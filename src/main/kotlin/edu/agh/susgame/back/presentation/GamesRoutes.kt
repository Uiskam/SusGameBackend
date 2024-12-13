package edu.agh.susgame.back.presentation

import edu.agh.susgame.back.services.rest.GamesRestImpl
import edu.agh.susgame.back.services.rest.GamesRestImpl.DeleteGameResult
import edu.agh.susgame.dto.rest.games.model.*
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.LobbyPin
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.future.await
import kotlinx.serialization.Serializable

val gamesRestImpl = GamesRestImpl()

@Serializable
data class HttpErrorResponseBody(val errorMessage: String)

val HttpUnknownErrorResponseBody = HttpErrorResponseBody("Unknown error")

fun Route.gameRouting() {
    route("/games") {
        getAllGamesRoute()
        getGameByIdRoute()
        getGameMapRoute()
        createGameRoute()
        deleteGameRoute()
        joinGameWebSocket()
    }
}

private fun Route.getAllGamesRoute() {
    get {
        val result = gamesRestImpl.getAllGames().await()
        call.respond(
            status = result.let { HttpStatusCode.fromValue(it.responseCode) },
            message = when (result) {
                is GetAllGamesApiResult.Success -> result.lobbies
                GetAllGamesApiResult.Error -> HttpUnknownErrorResponseBody
            }
        )
    }
}

private fun Route.getGameByIdRoute() {
    get("{gameId}") {
        val gameId = call.parameters["gameId"]
            ?.toIntOrNull()
            ?.let { LobbyId(it) }
            ?: run {
                call.respond(HttpStatusCode.BadRequest, HttpErrorResponseBody("Game id could not be casted to int!"))
                return@get
            }

        val gamePin = call.request.queryParameters["gamePin"]
            ?.let { LobbyPin(it) }

        val result = gamesRestImpl.getGameDetails(gameId, gamePin).await()

        call.respond(
            status = result.let { HttpStatusCode.fromValue(it.responseCode) },
            message = when (result) {
                is GetGameApiResult.Success -> result.lobby
                GetGameApiResult.DoesNotExist ->
                    HttpErrorResponseBody("Game with ${gameId.value} not found")

                GetGameApiResult.InvalidPin -> HttpErrorResponseBody("Invalid pin")
                GetGameApiResult.OtherError -> HttpUnknownErrorResponseBody
            }
        )
    }
}

private fun Route.getGameMapRoute() {
    get("map/{gameId}") {
        call.parameters["gameId"]
            ?.toInt()
            ?.let { LobbyId(it) }
            ?.let { lobbyId ->
                val result = gamesRestImpl.getGameMap(lobbyId).await()
                call.respond(
                    status = result.let { HttpStatusCode.fromValue(it.responseCode) },
                    message = when (result) {
                        is GetGameMapApiResult.Success -> result.gameMap
                        GetGameMapApiResult.GameDoesNotExist ->
                            HttpErrorResponseBody("Game ${lobbyId.value} was not found")

                        GetGameMapApiResult.GameNotYetStarted ->
                            HttpErrorResponseBody("Game ${lobbyId.value} is not yet started")

                        GetGameMapApiResult.OtherError -> HttpUnknownErrorResponseBody
                    }
                )
            }
    }
}

private fun Route.createGameRoute() {
    post {
        val request = call.receive<GameCreationRequest>()
        val result = gamesRestImpl.createGame(request.gameName, request.maxNumberOfPlayers, request.gamePin).await()
        call.respond(
            status = result.let { HttpStatusCode.fromValue(it.responseCode) },
            message = when (result) {
                is CreateGameApiResult.Success -> GameCreationApiResponse(result.createdLobbyId)
                CreateGameApiResult.NameAlreadyExists ->
                    HttpErrorResponseBody("Game with name ${request.gameName} already exists")

                CreateGameApiResult.OtherError -> HttpUnknownErrorResponseBody
            }
        )
    }
}

private fun Route.deleteGameRoute() {
    delete("{gameId}") {
        call.parameters["gameId"]
            ?.toInt()
            ?.let { gameId ->
                when (gamesRestImpl.deleteGame(gameId)) {
                    DeleteGameResult.Success -> call.respond(HttpStatusCode.OK)
                    DeleteGameResult.GameDoesNotExist ->
                        call.respond(HttpStatusCode.NotFound, HttpErrorResponseBody("Game with $gameId not found"))
                }
            }
    }
}

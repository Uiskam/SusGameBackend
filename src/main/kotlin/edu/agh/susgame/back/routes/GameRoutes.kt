package edu.agh.susgame.back.routes

import edu.agh.susgame.back.Connection
import edu.agh.susgame.back.models.Game
import edu.agh.susgame.back.models.GameStorage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.Serializable

var gameStorage = GameStorage()

@Serializable
data class ErrorObj(val errorMessage: String)

fun Route.gameRouting() {
    route("/games") {
        get {
            call.respond(gameStorage.getReturnableData())
        }
        get("{gameId}") {
            val gameId = call.parameters["gameId"]?.toInt()
            gameId?.let {
                gameStorage.findGameById(gameId)?.let {
                    call.respond(it.getDataToReturn().toString())
                } ?: call.respond(HttpStatusCode.NotFound, ErrorObj("Game with id $gameId not found"))
            }
        }

        @Serializable
        data class GameCreationRequest(
            val gameName: String,
            val maxNumberOfPlayers: Int = 4,
            val gamePin: String? = null
        )

        @Serializable
        data class GameCreationResponse(
            val gameId: Int
        )
        post {
            val request = call.receive<GameCreationRequest>()
            gameStorage.findGameByName(request.gameName)?.let {
                call.respond(HttpStatusCode.Conflict, ErrorObj("Game with name ${request.gameName} already exists"))
                //to jest do debaty czy chcemy wgl unikalnośc nazw gier
            } ?: run {
                val newGame = Game(request.gameName, request.maxNumberOfPlayers, request.gamePin)
                gameStorage.add(newGame)
                call.respond(HttpStatusCode.Created, GameCreationResponse(newGame.id))
            }
        }
        delete("{gameId}") {
            val gameId = call.parameters["gameId"]?.toInt()
            gameId?.let {
                val game = gameStorage.findGameById(gameId)
                if (game == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorObj("Game with $gameId not found"))
                } else {
                    gameStorage.remove(game)
                    call.respond(HttpStatusCode.OK)
                }
            }

        }

        webSocket("/join") {
            val gameId = call.request.queryParameters["gameId"]?.toIntOrNull()
            val playerName = call.request.queryParameters["playerName"]
            if (gameId == null || playerName == null) {
                close(
                    CloseReason(
                        CloseReason.Codes.CANNOT_ACCEPT,
                        ErrorObj("Missing gameName or playerName").toString()
                    )
                )
                return@webSocket
            }
            val game = gameStorage.findGameById(gameId)
            if (game == null) {
                close(
                    CloseReason(
                        CloseReason.Codes.CANNOT_ACCEPT,
                        ErrorObj("Game with id $gameId not found").toString()
                    )
                )
                return@webSocket
            }

            val thisConnection = Connection(this)
            game.addPlayer(thisConnection, playerName)
            try {
                send("You are connected! There are ${game.getPlayersCount()} users here.")
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    val playerMap = game.getPlayers()
                    playerMap.forEach {
                        if (it.key != thisConnection) {
                            it.key.session.send("[$playerName]: $receivedText")
                        }
                    }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Removing $thisConnection!")
                game.removePlayer(playerName)
            }

        }
    }

}
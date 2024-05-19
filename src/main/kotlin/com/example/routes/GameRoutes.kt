package com.example.routes

import com.example.Connection
import com.example.models.Game
import com.example.models.GameStorage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import java.util.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import com.example.*
import kotlinx.serialization.Serializable
import java.util.*
import kotlin.collections.LinkedHashSet

var gameStorage = GameStorage()

@Serializable
data class ErrorObj(val message: String)

fun Route.gameRouting() {
    route("/games") {
        get {
            call.respond(gameStorage.getReturnableData())
        }
        get("{gameId}") {
            val gameId = call.parameters["gameId"]?.toIntOrNull() ?:
            return@get call.respond(
                HttpStatusCode.BadRequest,
                ErrorObj("Missing query param: gameId")
            )

            val gameFound = gameStorage.findGame(gameId)
            if (gameFound == null) {
                call.respond(HttpStatusCode.BadRequest, ErrorObj("Game with id $gameId not found"))
            } else call.respond(gameFound.getDataToReturn().toString())
        }

        @Serializable
        data class GameCreationRequest(val gameName: String, val maxNumberOfPlayers: Int=4, val gamePin: String? = null)
        post {
            val request = call.receive<GameCreationRequest>()
            gameStorage.add(Game(request.gameName, request.maxNumberOfPlayers, request.gamePin))
            call.respondText("Game created correctly", status = HttpStatusCode.Created)
        }
        delete("{gameId}") {
            val gameId = call.parameters["gameId"]?.toIntOrNull() ?: return@delete call.respondText(
                "Missing name",
                status = HttpStatusCode.BadRequest
            )
            val game = gameStorage.findGame(gameId)
            if (game == null) {
                call.respond(HttpStatusCode.NotFound, ErrorObj("Game with $gameId not found"))
                return@delete
            }
            gameStorage.remove(game)
            call.respondText("Game removed correctly", status = HttpStatusCode.OK)
        }

        webSocket("/join") {
            val gameId = call.request.queryParameters["gameId"]?.toIntOrNull()
            val playerName = call.request.queryParameters["playerName"]
            if (gameId == null || playerName == null) {
                close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, ErrorObj("Missing gameName or playerName").toString()))
                return@webSocket
            }
            val game = gameStorage.findGame(gameId)
            if (game == null) {
                close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, ErrorObj("Game with id $gameId not found").toString()))
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
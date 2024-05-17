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
import java.util.*
import kotlin.collections.LinkedHashSet

var gameStorage = GameStorage()

fun Route.gameRouting() {
    route("/games") {
        get {
            println(";asbufaskhfbwsfvbkwjefvbwejkfvwejhfvbbfvjsdvbfjsdbvjhsdbvjhsdbvjsdbv")
            if (gameStorage.gameList.isNotEmpty()) {
                call.respond(gameStorage.getReturnableData())
            } else {
                call.respondText("No games found", status = HttpStatusCode.OK)
            }
        }
        get("{gameName}") {
            val gameName = call.parameters["gameName"] ?: return@get call.respondText(
                "Missing name",
                status = HttpStatusCode.BadRequest
            )
            val gameFound = gameStorage.findGame(gameName)
            if (gameFound == null) {
                call.respondText("Game not found", status = HttpStatusCode.NotFound)
            } else call.respond(gameFound.getDataToReturn().toString() ?: "Game not found")
        }
        post("{gameName}") {
            val gameName = call.parameters["gameName"] ?: return@post call.respondText(
                "Missing name",
                status = HttpStatusCode.BadRequest
            )
            gameStorage.add(Game(gameName))
            call.respondText("Game stored correctly", status = HttpStatusCode.Created)
        }
        delete("{gameName}") {
            val gameName = call.parameters["gameName"] ?: return@delete call.respondText(
                "Missing name",
                status = HttpStatusCode.BadRequest
            )
            gameStorage.remove(gameName)
            call.respondText("Game removed correctly", status = HttpStatusCode.BadRequest)
        }

        webSocket("/join") {
            println(";asbufaskhfbwsfvbkwjefvbwejkfvwejhfvbbfvjsdvbfjsdbvjhsdbvjhsdbvjsdbv")
            val gameName = call.request.queryParameters["gameName"]
            val playerName = call.request.queryParameters["playerName"]
            if (gameName == null || playerName == null) {
                close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Missing gameName or playerName"))
                return@webSocket
            }
            val game = gameStorage.findGame(gameName)
            if (game == null) {
                close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Game not found"))
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
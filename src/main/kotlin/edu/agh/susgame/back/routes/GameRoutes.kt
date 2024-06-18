package edu.agh.susgame.back.routes

import edu.agh.susgame.back.Connection
import edu.agh.susgame.back.models.Game
import edu.agh.susgame.back.models.GameStorage
import edu.agh.susgame.dto.ClientSocketMessage
import edu.agh.susgame.dto.ServerSocketMessage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray

var gameStorage = GameStorage(
    gameList = listOf(
        Game(
            name = "Gra nr 1",
            maxNumberOfPlayers = 5,
        ),
        Game(
            name = "Gra inna",
            maxNumberOfPlayers = 6,
            gamePin = "pin",
        ),
        Game(
            name = "Gra III",
            maxNumberOfPlayers = 3,
        ),
    ).toMutableList()
)

@Serializable
data class ErrorObj(val errorMessage: String)

@OptIn(ExperimentalSerializationApi::class)
private val cbor = Cbor

@OptIn(ExperimentalSerializationApi::class)
fun Route.gameRouting() {
    route("/games") {
        get {
            call.respond(gameStorage.getReturnableData())
        }
        get("{gameId}") {
            val gameId = call.parameters["gameId"]?.toInt()
            gameId?.let {
                gameStorage.findGameById(gameId)?.let {
                    call.respond(it.getDataToReturn())
                } ?: call.respond(HttpStatusCode.NotFound, ErrorObj("Game with id $gameId not found"))
            }
        }

        @Serializable
        data class GameCreationRequest(
            val gameName: String,
            val maxNumberOfPlayers: Int = 4,
            val gamePin: String? = null,
        )

        @Serializable
        data class GameCreationResponse(
            val gameId: Int,
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
                for (frame in incoming) {
                    frame as? Frame.Binary ?: continue
                    val receivedMessage = cbor.decodeFromByteArray<ClientSocketMessage>(frame.data)

                    when (receivedMessage) {
                        is ClientSocketMessage.ChatMessage -> {
                            val playerMap = game.getPlayers()
                            playerMap.forEach {
                                val connection = it.key
                                val playerNickname = it.value

                                if (connection != thisConnection) {
                                    val serverMessage: ServerSocketMessage = ServerSocketMessage.ChatMessage(
                                        authorNickname = playerNickname,
                                        message = receivedMessage.message,
                                    )

                                    val encodedServerMessage = cbor.encodeToByteArray(serverMessage)
                                    connection.session.send(encodedServerMessage)
                                }
                            }

                        }
                        else -> {}
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

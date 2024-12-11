package edu.agh.susgame.back.presentation

import edu.agh.susgame.back.domain.net.Player
import edu.agh.susgame.back.services.socket.GamesWebSocketConnection
import edu.agh.susgame.dto.socket.ClientSocketMessage
import edu.agh.susgame.dto.socket.ServerSocketMessage
import edu.agh.susgame.dto.socket.common.GameStatus
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray

@OptIn(ExperimentalSerializationApi::class)
fun Route.joinGameWebSocket() {
    webSocket("/join") {
        val gameId = call.request.queryParameters["gameId"]?.toIntOrNull() ?: run {
            closeConnection(this, "Missing gameId")
            return@webSocket
        }
        val playerName = call.request.queryParameters["playerName"] ?: run {
            closeConnection(this, "Missing playerName")
            return@webSocket
        }
        val playerId = call.request.queryParameters["playerId"]?.toIntOrNull()

        val gamePin = call.request.queryParameters["gamePin"]

        val game = gamesRestImpl.findGameById(gameId) ?: run {
            closeConnection(this, "Game with id $gameId not found")
            return@webSocket
        }

        if (!game.checkPinMatch(gamePin)) {
            closeConnection(this, "Game pin does not match")
            return@webSocket
        }

        val thisConnection = GamesWebSocketConnection(this)
        val thisPlayer: Player
        try {
            thisPlayer = when {
                (game.getGameStatus() == GameStatus.WAITING && playerId == null) -> {
                    game.addPlayer(thisConnection, playerName)
                }

                (game.getGameStatus() == GameStatus.RUNNING && playerId != null) -> {
                    game.reconnectPlayer(thisConnection, playerId)
                }

                else -> {
                    closeConnection(this, "Game is not in correct state to join")
                    return@webSocket
                }
            }
        } catch (e: IllegalArgumentException) {
            thisConnection.sendServerSocketMessage(
                ServerSocketMessage.ServerError(errorMessage = e.message ?: "Unknown error")
            )
            closeConnection(this, "Connection could not be established")
            return@webSocket
        }
        try {
            for (frame in incoming) {

                frame as? Frame.Binary ?: continue

                when (val receivedMessage = Cbor.decodeFromByteArray<ClientSocketMessage>(frame.data)) {
                    // Handle lobby
                    is ClientSocketMessage.PlayerChangeReadiness -> game.handlePlayerChangeReadinessRequest(
                        thisConnection,
                        thisPlayer,
                        receivedMessage
                    )

                    is ClientSocketMessage.PlayerChangeColor -> game.handlePlayerChangeColor(
                        thisConnection,
                        thisPlayer,
                        receivedMessage
                    )

                    is ClientSocketMessage.PlayerLeaving -> game.handlePlayerLeavingRequest(
                        thisConnection,
                        thisPlayer
                    )

                    // Handle game
                    is ClientSocketMessage.ChatMessage -> game.handleChatMessage(
                        thisConnection,
                        thisPlayer,
                        receivedMessage
                    )

                    is ClientSocketMessage.GameState -> game.handleGameState(receivedMessage, this)

                    is ClientSocketMessage.HostRouteDTO -> game.handleHostRoute(receivedMessage)

                    is ClientSocketMessage.HostFlowDTO -> game.handleHostFlow(receivedMessage)

                    is ClientSocketMessage.UpgradeDTO -> game.handleUpgradeDTO(
                        thisConnection,
                        receivedMessage,
                        thisPlayer
                    )

                    is ClientSocketMessage.FixRouterDTO -> game.handleFixRouterDTO(thisConnection, receivedMessage)

                    is ClientSocketMessage.QuizAnswerDTO -> game.handleQuizAnswerDTO(
                        receivedMessage,
                        thisPlayer
                    )
                }
            }
        } catch (e: Exception) {
            println(e.localizedMessage)
        } finally {
            println("Removing $thisConnection!")
            game.removePlayer(thisConnection)
        }
    }
}

suspend fun closeConnection(
    webSocketSession: DefaultWebSocketSession,
    errorMessage: String
) {
    webSocketSession.close(
        CloseReason(
            CloseReason.Codes.CANNOT_ACCEPT,
            HttpErrorResponseBody(errorMessage).toString()
        )
    )
}
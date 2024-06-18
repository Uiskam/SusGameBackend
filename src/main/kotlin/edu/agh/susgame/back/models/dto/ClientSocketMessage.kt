// WARNING: THIS FILE WAS CLONED AUTOMATICALLY FROM 'SusGameDTO' GITHUB REPOSITORY
// IT SHOULD NOT BE EDITED IN ANY WAY
// IN ORDER TO CHANGE THIS DTO, COMMIT TO 'SusGameDTO' GITHUB REPOSITORY
// IN ORDER TO UPDATE THIS FILE TO NEWEST VERSION, RUN 'scripts/update-DTO.sh'

package edu.agh.susgame.dto

import edu.agh.susgame.dto.common.GameStatus
import kotlinx.serialization.Serializable

/**
 * Message sent by client via WebSocket
 */
@Serializable
sealed class ClientSocketMessage {
    @Serializable
    data class HostDTO(
        val id: Int,
        val packetPath: List<Int>,
        val packetsSentPerTick: Int,
    ) : ClientSocketMessage()

    @Serializable
    data class UpgradeDTO(
        val deviceId: Int,
    ) : ClientSocketMessage()

    /**
     * Used to initialize a game
     */
    @Serializable
    data class GameState(
        val gameStatus: GameStatus,
    ) : ClientSocketMessage()

    /**
     * Used for chat feature, which is planned to be deleted in the future
     */
    @Serializable
    data class ChatMessage(
        val message: String,
    ) : ClientSocketMessage()
}

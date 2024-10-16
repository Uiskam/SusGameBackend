// WARNING: THIS FILE WAS CLONED AUTOMATICALLY FROM 'SusGameDTO' GITHUB REPOSITORY
// IT SHOULD NOT BE EDITED IN ANY WAY
// IN ORDER TO CHANGE THIS DTO, COMMIT TO 'SusGameDTO' GITHUB REPOSITORY
// IN ORDER TO UPDATE THIS FILE TO NEWEST VERSION, RUN 'scripts/update-DTO.sh'

package edu.agh.susgame.dto.rest.model

import kotlinx.serialization.Serializable
import edu.agh.susgame.dto.socket.server.PlayerDTO

@Serializable
data class PlayerId(val value: Int)

@Serializable
data class PlayerNickname(val value: String)

@Serializable
data class Player(
    val nickname: PlayerNickname,
    val id: PlayerId,
    val colorHex: Long,
) {
    //TODO Widze to że to trzeba zmienić jakkoś jak sa Ci playerzy bo to jest z DTO i tego nie można zmieniać
    fun toNetPlayer(): edu.agh.susgame.back.net.Player {
        return edu.agh.susgame.back.net.Player(id.value, nickname.value)
    }

    fun toDTO(): PlayerDTO {
        return PlayerDTO(id.value, nickname.value, 0)
    }
}

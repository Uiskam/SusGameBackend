// WARNING: THIS FILE WAS CLONED AUTOMATICALLY FROM 'SusGameDTO' GITHUB REPOSITORY
// IT SHOULD NOT BE EDITED IN ANY WAY
// IN ORDER TO CHANGE THIS DTO, COMMIT TO 'SusGameDTO' GITHUB REPOSITORY
// IN ORDER TO UPDATE THIS FILE TO NEWEST VERSION, RUN 'scripts/update-DTO.sh'

package edu.agh.susgame.dto.rest.model

import kotlinx.serialization.Serializable


@Serializable
data class LobbyId(val value: Int)

@Serializable
data class LobbyPin(val value: String)

@Serializable
data class LobbyRow(
    val id: LobbyId,
    val name: String,
    val isPinSetUp: Boolean,
    val maxNumOfPlayers: Int,
    val playersWaitingCount: Int,
)

@Serializable
data class LobbyDetails(
    val id: LobbyId,
    val name: String,
    val isPinSetUp: Boolean,
    val maxNumOfPlayers: Int,
    val playersWaiting: List<PlayerREST>,
)

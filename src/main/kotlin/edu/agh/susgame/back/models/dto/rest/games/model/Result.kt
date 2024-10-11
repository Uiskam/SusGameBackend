// WARNING: THIS FILE WAS CLONED AUTOMATICALLY FROM 'SusGameDTO' GITHUB REPOSITORY
// IT SHOULD NOT BE EDITED IN ANY WAY
// IN ORDER TO CHANGE THIS DTO, COMMIT TO 'SusGameDTO' GITHUB REPOSITORY
// IN ORDER TO UPDATE THIS FILE TO NEWEST VERSION, RUN 'scripts/update-DTO.sh'

package edu.agh.susgame.dto.rest.games.model

import edu.agh.susgame.dto.rest.model.Lobby
import edu.agh.susgame.dto.rest.model.LobbyId


sealed class GetAllGamesApiResult {
    data class Success(val lobbies: List<Lobby>) : GetAllGamesApiResult()
    data object Error : GetAllGamesApiResult()
}

sealed class GetGameApiResult {
    data class Success(val lobby: Lobby) : GetGameApiResult()
    data object DoesNotExist : GetGameApiResult()
    data object OtherError : GetGameApiResult()
}

sealed class CreateGameApiResult {
    data class Success(val createdLobbyId: LobbyId) : CreateGameApiResult()
    data object NameAlreadyExists : CreateGameApiResult()
    data object OtherError : CreateGameApiResult()
}

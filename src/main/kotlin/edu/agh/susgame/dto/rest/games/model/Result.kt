// WARNING: THIS FILE WAS CLONED AUTOMATICALLY FROM 'SusGameDTO' GITHUB REPOSITORY
// IT SHOULD NOT BE EDITED IN ANY WAY
// IN ORDER TO CHANGE THIS DTO, COMMIT TO 'SusGameDTO' GITHUB REPOSITORY
// IN ORDER TO UPDATE THIS FILE TO NEWEST VERSION, RUN 'scripts/update-DTO.sh'

package edu.agh.susgame.dto.rest.games.model

import edu.agh.susgame.dto.rest.ApiResult
import edu.agh.susgame.dto.rest.model.GameMapDTO
import edu.agh.susgame.dto.rest.model.LobbyDetails
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.LobbyRow
import java.net.HttpURLConnection


sealed class GetAllGamesApiResult(responseCode: Int) : ApiResult(responseCode) {
    data class Success(val lobbies: List<LobbyRow>) : GetAllGamesApiResult(HttpURLConnection.HTTP_OK)

    data object Error : GetAllGamesApiResult(HttpURLConnection.HTTP_INTERNAL_ERROR)
}


sealed class GetGameApiResult(responseCode: Int) : ApiResult(responseCode) {
    data class Success(val lobby: LobbyDetails) : GetGameApiResult(HttpURLConnection.HTTP_OK)

    data object DoesNotExist : GetGameApiResult(HttpURLConnection.HTTP_NOT_FOUND)

    data object InvalidPin : GetGameApiResult(HttpURLConnection.HTTP_UNAUTHORIZED)

    data object OtherError : GetGameApiResult(HttpURLConnection.HTTP_INTERNAL_ERROR)
}


sealed class GetGameMapApiResult(responseCode: Int) : ApiResult(responseCode) {
    data class Success(
        val gameMap: GameMapDTO,
    ) : GetGameMapApiResult(HttpURLConnection.HTTP_OK)

    data object GameDoesNotExist : GetGameMapApiResult(HttpURLConnection.HTTP_NOT_FOUND)

    data object GameNotYetStarted : GetGameMapApiResult(HttpURLConnection.HTTP_BAD_REQUEST)

    data object OtherError : GetGameMapApiResult(HttpURLConnection.HTTP_INTERNAL_ERROR)
}


sealed class CreateGameApiResult(responseCode: Int) : ApiResult(responseCode) {
    data class Success(val createdLobbyId: LobbyId) : CreateGameApiResult(HttpURLConnection.HTTP_CREATED)

    data object NameAlreadyExists : CreateGameApiResult(HttpURLConnection.HTTP_CONFLICT)

    data object OtherError : CreateGameApiResult(HttpURLConnection.HTTP_INTERNAL_ERROR)
}

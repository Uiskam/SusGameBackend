package edu.agh.susgame.back.rest.games

import edu.agh.susgame.back.models.Game
import edu.agh.susgame.back.models.GameStorage
import edu.agh.susgame.dto.rest.games.GamesRest
import edu.agh.susgame.dto.rest.games.model.CreateGameApiResult
import edu.agh.susgame.dto.rest.games.model.GetAllGamesApiResult
import edu.agh.susgame.dto.rest.games.model.GetGameApiResult
import edu.agh.susgame.dto.rest.model.LobbyId
import java.util.concurrent.CompletableFuture

class GamesRestImpl : GamesRest {
    var gameStorage = GameStorage(
        gameList = listOf(
            Game(
                name = "Gra do testowania v0.1 engine",
                maxNumberOfPlayers = 4,
            ),
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

    override fun getAllGames(): CompletableFuture<GetAllGamesApiResult> {
        return CompletableFuture.supplyAsync {
            GetAllGamesApiResult.Success(gameStorage.getReturnableData())
        }
    }

    override fun getGame(gameId: LobbyId): CompletableFuture<GetGameApiResult> {
        return CompletableFuture.supplyAsync {
            gameStorage.findGameById(gameId.value)?.let { game ->
                GetGameApiResult.Success(game.getDataToReturn())
            } ?: GetGameApiResult.DoesNotExist
        }
    }

    override fun createGame(
        gameName: String,
        maxNumberOfPlayers: Int,
        gamePin: String?
    ): CompletableFuture<CreateGameApiResult> {
        return CompletableFuture.supplyAsync {
            gameStorage.findGameByName(gameName)?.let {
                CreateGameApiResult.NameAlreadyExists
            } ?: run {
                val newGame = Game(gameName, maxNumberOfPlayers, gamePin)
                gameStorage.add(newGame)
                // TODO GAME-74 Suggestion: Propagate the usage of `LobbyId` on backend
                CreateGameApiResult.Success(createdLobbyId = LobbyId(newGame.id))
            }
        }
    }

    sealed class DeleteGameResult {
        data object Success : DeleteGameResult()
        data object GameDoesNotExist : DeleteGameResult()
    }

    // This method is not a part of contract (frontend doesn't use it), but it can stay for now
    fun deleteGame(gameId: Int): DeleteGameResult {
        val game = gameStorage.findGameById(gameId)

        if (game == null) {
            return DeleteGameResult.GameDoesNotExist
        } else {
            gameStorage.remove(game)
            return DeleteGameResult.Success
        }
    }
}

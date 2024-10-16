package edu.agh.susgame.back.models

import edu.agh.susgame.back.Connection
import edu.agh.susgame.dto.rest.model.*
import edu.agh.susgame.dto.socket.common.GameStatus
import edu.agh.susgame.back.net.NetGraph
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random
import edu.agh.susgame.dto.rest.model.Player

class Game(
    val name: String,
    val maxNumberOfPlayers: Int,
    val gamePin: String? = null,
    var gameStatus: GameStatus = GameStatus.WAITING
) {
    companion object {
        val lastId = AtomicInteger(0)
    }

    val id = lastId.getAndIncrement()

    private val playerMap: MutableMap<Connection, Player> = ConcurrentHashMap()

    fun addPlayer(connection: Connection, playerName: String) {
        if (playerMap.values.any { it.nickname.value == playerName }) {
            throw IllegalArgumentException("Player with name $playerName already exists")
        }
        playerMap[connection] =
            Player(PlayerNickname(playerName), PlayerId(playerMap.size), Random.nextLong(0, 0xFFFFFF))
    }

    fun removePlayer(playerName: String) {
        playerMap.entries.removeIf { it.value.nickname.value == playerName }
    }

    fun getDataToReturn(): Lobby {
        return Lobby(
            id = LobbyId(id),
            name = name,
            maxNumOfPlayers = maxNumberOfPlayers,
            // TODO GAME-74 Remove this hardcoded value
            gameTime = 10,
            playersWaiting = playerMap.values.toList(),
        )
    }

    fun getPlayers(): MutableMap<Connection, Player> {
        return playerMap
    }
}

class GameStorage(var gameList: MutableList<Game> = mutableListOf()) {
    fun add(game: Game) {
        gameList.add(game)
    }

    fun remove(game: Game) {
        gameList.remove(game)
    }

    fun findGameById(gameId: Int): Game? {
        return gameList.find { it.id == gameId }
    }

    fun findGameByName(gameName: String): Game? {
        return gameList.find { it.name == gameName }
    }

    fun getReturnableData(): List<Lobby> {
        return gameList.map { it.getDataToReturn() }
    }

}
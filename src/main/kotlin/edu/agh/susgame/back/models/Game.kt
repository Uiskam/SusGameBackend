package edu.agh.susgame.back.models

import edu.agh.susgame.back.Connection
import edu.agh.susgame.dto.rest.model.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

class Game(
    val name: String,
    val maxNumberOfPlayers: Int,
    val gamePin: String? = null
) {
    companion object {
        val lastId = AtomicInteger(0)
    }

    val id = lastId.getAndIncrement()

    private val playerMap: MutableMap<Connection, String> = ConcurrentHashMap()
    fun addPlayer(connection: Connection, playerName: String) {
        if (playerMap.containsValue(playerName)) {
            throw IllegalArgumentException("Player with name $playerName already exists")
        }
        playerMap[connection] = playerName
    }

    fun removePlayer(playerName: String) {
        playerMap.entries.removeIf { it.value == playerName }
    }

    fun getDataToReturn(): Lobby {
        return Lobby(
            id = LobbyId(id),
            name = name,
            maxNumOfPlayers = maxNumberOfPlayers,
            // TODO GAME-74 Remove this hardcoded value
            gameTime = 10,
            playersWaiting = playerMap.values.toList().map { nickname ->
                Player(
                    // TODO GAME-74 Players are not properly indexed
                    id = PlayerId(nickname.hashCode()),
                    nickname = PlayerNickname(nickname),
                    // TODO GAME-74 Player color (HEX value) should be constant for each player
                    colorHex = Random.nextLong(0, 0xFFFFFF),
                )
            }
        )
    }

    fun getPlayers(): MutableMap<Connection, String> {
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
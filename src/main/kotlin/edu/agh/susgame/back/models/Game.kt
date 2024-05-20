package edu.agh.susgame.back.models

import edu.agh.susgame.back.Connection
import kotlinx.serialization.Serializable
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@Serializable
data class GameReturnData(
    val id: Int,
    val name: String,
    val maxNumberOfPlayers: Int,
    val players: List<String>
)

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

    fun getPlayersCount(): Int {
        return playerMap.size
    }

    fun getDataToReturn(): GameReturnData {
        return GameReturnData(id, name, maxNumberOfPlayers, playerMap.values.toList())
    }

    fun getPlayers(): MutableMap<Connection, String> {
        return playerMap
    }
}

class GameStorage(var gameList: MutableList<Game> = mutableListOf()) {
    fun add(game: Game) {
        if (this.findGame(game.id) != null) {
            throw IllegalArgumentException("Game with name ${game.id} already exists")
        }
        gameList.add(game)
    }

    fun remove(game: Game) {
        gameList.remove(game)
    }

    fun findGame(gameId: Int): Game? {
        return gameList.find { it.id == gameId }
    }

    fun getAllGames(): List<Game> {
        return gameList
    }

    fun getReturnableData(): List<GameReturnData> {
        return gameList.map { it.getDataToReturn() }
    }

}
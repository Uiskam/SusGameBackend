package edu.agh.susgame.back.models

import edu.agh.susgame.back.socket.GamesWebSocketConnection
import edu.agh.susgame.back.net.NetGraph
import edu.agh.susgame.back.net.Player
import edu.agh.susgame.dto.rest.model.*
import edu.agh.susgame.dto.socket.common.GameStatus
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class Game(
    val name: String,
    val maxNumberOfPlayers: Int,
    val gamePin: String? = null,
    var gameStatus: GameStatus = GameStatus.WAITING,
    var gameGraph: NetGraph = NetGraph(),
) {
    companion object {
        val lastId = AtomicInteger(0)
    }

    val id = lastId.getAndIncrement()

    private val playerMap: MutableMap<GamesWebSocketConnection, Player> = ConcurrentHashMap()

    fun addPlayer(connection: GamesWebSocketConnection, newPlayer: Player) {
        if (playerMap.values.any { it.name == newPlayer.name }) {
            throw IllegalArgumentException("Player with name $newPlayer.name already exists")
        }
        playerMap[connection] = Player(index = playerMap.size, name = newPlayer.name)
    }

    fun removePlayer(playerName: String) {
        playerMap.entries.removeIf { it.value.name == playerName }
    }

    fun getDataToReturn(): Lobby {
        return Lobby(
            id = LobbyId(id),
            name = name,
            maxNumOfPlayers = maxNumberOfPlayers,
            // TODO GAME-74 Remove this hardcoded value
            gameTime = 10,
            playersWaiting = playerMap.values.map { it.toREST() },
        )
    }

    fun getPlayers(): MutableMap<GamesWebSocketConnection, Player> {
        return playerMap
    }

    fun addMoneyForAllPlayers() {
        playerMap.values.forEach { it.addMoney() }
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
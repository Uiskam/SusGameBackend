package com.example.models

import com.example.Connection
import java.util.concurrent.ConcurrentHashMap


data class Game(val name:String, val playerMap:MutableMap<Connection, String> = ConcurrentHashMap()) {
    fun addPlayer(connection: Connection, playerName: String) {
        if (playerMap.containsValue(playerName)) {
            throw IllegalArgumentException("Player with name $playerName already exists")
        }
        playerMap[connection] = playerName
    }
    fun removePlayer(playerName: String) {
        playerMap.entries.removeIf { it.value == playerName }
    }
    fun getPlayerNames(): List<String> {
        return playerMap.values.toList()
    }
    fun getPlayersCount(): Int {
        return playerMap.size
    }
    fun getDataToReturn(): Pair<String, List<String>> {
        return Pair(name, getPlayerNames())
    }
    fun getPlayers(): MutableMap<Connection, String> {
        return playerMap
    }
}

class GameStorage(var gameList:MutableList<Game> = mutableListOf()) {
    fun add(game: Game) {
        if (this.findGame(game.name) != null) {
            throw IllegalArgumentException("Game with name ${game.name} already exists")
        }
        gameList.add(game)
    }
    fun remove(gameName: String) {
        val game = findGame(gameName) ?: throw IllegalArgumentException("Game with name $gameName not found")
        gameList.remove(game)
    }
    fun findGame(name: String): Game? {
        return gameList.find { it.name == name }
    }
    fun getAllGames(): List<Game> {
        return gameList
    }
    fun getReturnableData(): List<Pair<String, List<String>>> {
        return gameList.map { it.getDataToReturn() }
    }

}
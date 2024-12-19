package edu.agh.susgame.back.domain.build

import edu.agh.susgame.back.domain.net.Player
import java.io.File
import kotlin.random.Random


object GameInitializer {
    fun getGameParams(players: List<Player>): GameConfig {
        val directoryPath = "src/main/resources/game_files/${players.size}"
        val directory = File(directoryPath)
        var files = directory.listFiles { _, name -> name.endsWith(".json") }
        files = directory.listFiles { _, name -> name.endsWith(".json") && name.contains("new") }


        val randomFile = if (files != null && files.isNotEmpty()) {
            files[Random.nextInt(files.size)]
        } else {
            throw IllegalArgumentException("No game map for ${players.size} players!")
        }

        return GameConfigParser().parseFromFile(randomFile.absolutePath, players)
    }
}
package edu.agh.susgame.back.domain.build

import edu.agh.susgame.back.domain.net.Player
import java.io.File
import kotlin.random.Random


object GameInitializer {
    fun getGameParams(players: List<Player>): GameConfig {
        val directoryPath = "game_files/${players.size}"
        val directory = File(directoryPath)
        val files = directory.listFiles { _, name -> name.endsWith(".json") }
        val randomFile = if (files != null && files.isNotEmpty()) {
            files[Random.nextInt(files.size)]
        } else {
            println("No game map for ${players.size} players! (directory: ${directory.absolutePath})")
            throw IllegalArgumentException("No game map for ${players.size} players! (directory: ${directory.absolutePath})")
        }


        return GameConfigParser().parseFromFile(randomFile.absolutePath, players)
    }
}
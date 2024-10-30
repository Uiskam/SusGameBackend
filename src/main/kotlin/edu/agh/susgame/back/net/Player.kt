package edu.agh.susgame.back.net

import edu.agh.susgame.config.MONEY_GAIN_PER_ITERATION
import edu.agh.susgame.config.PLAYER_BASE_MONEY
import edu.agh.susgame.dto.socket.server.PlayerDTO
import edu.agh.susgame.dto.rest.model.*
import kotlin.random.Random
import edu.agh.susgame.dto.rest.model.PlayerREST

class Player(
    private val index: Int,
    val name: String,
    private val colorHex: Long = Random.nextLong(0, 0xFFFFFF),
    private var currentMoney: Int = PLAYER_BASE_MONEY,
) {
    fun toREST(): PlayerREST {
        return PlayerREST(
            nickname = PlayerNickname(name),
            id = PlayerId(index),
            colorHex = colorHex
        )
    }

    fun toDTO(): PlayerDTO {
        return PlayerDTO(id = index, playerName = name, currentMoney = currentMoney)
    }

    fun getCurrentMoney(): Int {
        return currentMoney
    }

    fun setCurrentMoney(money: Int) {
        currentMoney = money
    }

    /**
     * Adds money to the player's current balance.
     * Increments the player's current money by a predefined amount defined in the configuration.
     */
    fun addMoney() {
        currentMoney += MONEY_GAIN_PER_ITERATION
    }

}
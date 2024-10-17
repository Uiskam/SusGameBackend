package edu.agh.susgame.back.net

import edu.agh.susgame.dto.socket.server.PlayerDTO
import edu.agh.susgame.dto.rest.model.*
import kotlin.random.Random
import edu.agh.susgame.dto.rest.model.PlayerREST

class Player(
    private val index: Int,
    val name: String,
    private val colorHex: Long = Random.nextLong(0, 0xFFFFFF),
    private var currentMoney: Int = 0,
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

}
package edu.agh.susgame.back.rest.games

import edu.agh.susgame.back.models.Game
import edu.agh.susgame.dto.socket.ServerSocketMessage


object ServerSocketMessageParser {

    fun gameToGameState(game: Game) = ServerSocketMessage.GameState(
        routers = game.gameGraph.getRoutersList()
            .map { it.toDTO() },
        server = game.gameGraph.getServer().toDTO(),
        hosts = game.gameGraph.getHostsList().map { it.toDTO() },
        edges = game.gameGraph.getEdges().map { it.toDTO() },
        players = game.getPlayers().toMap().values.map { it.toDTO() },
        gameStatus = game.gameStatus,
    )
}

package edu.agh.susgame.back.rest.games

import edu.agh.susgame.back.models.Game
import edu.agh.susgame.dto.socket.ServerSocketMessage


object ServerSocketMessageParser {

    fun gameToGameState(game: Game) = ServerSocketMessage.GameState(
        routers = game.netGraph.getRoutersList()
            .map { it.toDTO() },
        server = game.netGraph.getServer().toDTO(),
        hosts = game.netGraph.getHostsList().map { it.toDTO() },
        edges = game.netGraph.getEdges().map { it.toDTO() },
        players = game.getPlayers().toMap().values.map { it.toDTO() },
        gameStatus = game.getGameStatus(),
    )
}

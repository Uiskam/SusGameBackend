package edu.agh.susgame.back.rest.games

import edu.agh.susgame.back.models.Game
import edu.agh.susgame.back.net.NetGraph
import edu.agh.susgame.dto.rest.model.GameMapDTO
import edu.agh.susgame.dto.rest.model.GameMapEdgeDTO
import edu.agh.susgame.dto.rest.model.GameMapNodeDTO
import edu.agh.susgame.dto.socket.ServerSocketMessage


object RestParser {

    fun gameToGameState(game: Game) = ServerSocketMessage.GameState(
        routers = game.netGraph.getRoutersList()
            .map { it.toDTO() },
        server = game.netGraph.getServer().toDTO(),
        hosts = game.netGraph.getHostsList().map { it.toDTO() },
        edges = game.netGraph.getEdges().map { it.toDTO() },
        players = game.getPlayers().toMap().values.map { it.toDTO() },
        gameStatus = game.getGameStatus(),
    )

    fun netGraphToGetGameMapDTO(netGraph: NetGraph): GameMapDTO {
        val server = GameMapNodeDTO.Server(
            id = netGraph.getServer().index,
            coordinates = netGraph.getServer().getCoordinates(),
        )

        val hosts = netGraph.getHostsList().map { host ->
            GameMapNodeDTO.Host(
                id = host.index,
                coordinates = host.getCoordinates(),
            )
        }

        val routers = netGraph.getRoutersList().map { router ->
            GameMapNodeDTO.Router(
                id = router.index,
                coordinates = router.getCoordinates(),
                bufferSize = router.getBufferSize(),
            )
        }

        return GameMapDTO(
            nodes = hosts + routers + server,
            edges = netGraph.getEdges().toList().map { edge ->
                val (fromNodeId, toNodeId) = edge.connectedNodesIds
                GameMapEdgeDTO(
                    from = fromNodeId,
                    to = toNodeId,
                    weight = edge.getWeight()
                )
            },
        )
    }
}

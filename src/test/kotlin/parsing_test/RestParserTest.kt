package net_test.parsing_test

import edu.agh.susgame.back.net.Edge
import edu.agh.susgame.back.net.NetGraph
import edu.agh.susgame.back.net.Player
import edu.agh.susgame.back.net.node.Host
import edu.agh.susgame.back.net.node.Router
import edu.agh.susgame.back.net.node.Server
import edu.agh.susgame.back.rest.games.RestParser
import edu.agh.susgame.dto.rest.model.Coordinates
import edu.agh.susgame.dto.rest.model.GameMapNodeDTO.Host as HostDTO
import edu.agh.susgame.dto.rest.model.GameMapNodeDTO.Server as ServerDTO
import edu.agh.susgame.dto.rest.model.GameMapNodeDTO.Router as RouterDTO
import edu.agh.susgame.dto.rest.model.GameMapEdgeDTO as EdgeDTO
import net_test.TestUtils
import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RestParserTest : TestUtils {
    @Test
    fun `parsing net graph to GameMapApiResult`() {
        val graph = NetGraph()

        // Create graph elements
        val player0 = Player(0, "Player0")

        val Coordinates1 = Coordinates(2, 1)
        val Coordinates2 = Coordinates(3, 7)
        val Coordinates3 = Coordinates(0xACAB, 0xACAB)
        val Coordinates4 = Coordinates(966, 1410)

        // nodes
        // host
        val node1 = Host(0, asPair(Coordinates1), player0)
        //routers
        val node2 = Router(1, asPair(Coordinates2),  5)
        val node3 = Router(2, asPair(Coordinates3), 2)
        // server
        val node4 = Server(3, asPair(Coordinates4))

        // Add elements to the structure
        graph.addNode(node1)
        graph.addNode(node2)
        graph.addNode(node3)
        graph.addNode(node4)

        // edges
        val edge0 = Edge(0, 32, connectedNodesIds = Pair(0, 1))
        val edge1 = Edge(1, 64, connectedNodesIds = Pair(0, 2))
        val edge2 = Edge(2, 128, connectedNodesIds = Pair(1, 2))
        val edge3 = Edge(3, 256, connectedNodesIds = Pair(2, 3))

        graph.addEdge(node1, node2, edge0) // host - router1
        graph.addEdge(node1, node3, edge1) // host - router2
        graph.addEdge(node2, node3, edge2) // router2 - router1
        graph.addEdge(node3, node4, edge3) // router2 - server

        val result = RestParser.netGraphToGetGameMapApiResult(graph)

        assertEquals(result.responseCode, 200)

        assertEquals(result.nodes.size, 4)
        assertContains(result.nodes, HostDTO(id = 0, coordinates = Coordinates1))
        assertContains(result.nodes, RouterDTO(id = 1, coordinates = Coordinates2, bufferSize = 5))
        assertContains(result.nodes, RouterDTO(id = 2, coordinates = Coordinates3, bufferSize = 2))
        assertContains(result.nodes, ServerDTO(id = 3, coordinates = Coordinates4))

        assertEquals(result.edges.size, 4)
        assertContains(result.edges, EdgeDTO(from = 0, to = 1, weight = 32))
        assertContains(result.edges, EdgeDTO(from = 0, to = 2, weight = 64))
        assertContains(result.edges, EdgeDTO(from = 1, to = 2, weight = 128))
        assertContains(result.edges, EdgeDTO(from = 2, to = 3, weight = 256))
    }

    private fun asPair(coordinates: Coordinates): Pair<Int, Int> = Pair(coordinates.x, coordinates.y)
}

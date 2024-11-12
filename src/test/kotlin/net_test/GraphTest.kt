package net_test

import edu.agh.susgame.back.net.*
import edu.agh.susgame.back.net.node.*

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse


class GraphTest {

    /**
     * Tests if the graph is properly initialized.
     */
    @Test
    fun `relations between structure elements`(){
        val graph = NetGraph()

        // Create graph elements
        val player0 = Player(0, "Player0")

        // nodes
        // host
        val node0 = Host(0, player0)
        //routers
        val node1 = Router(1, 5)
        val node2 = Router(2, 5)
        // server
        val node3 = Server(3)


        // edges
        val edge0 = Edge(0, 5)
        val edge1 = Edge(1, 5)
        val edge2 = Edge(2, 5)
        val edge3 = Edge(3, 5)

        // Add elements to the structure
        graph.addNode(node1)
        graph.addNode(node2)
        graph.addNode(node3)
        graph.addNode(node0)

        graph.addEdge(node0, node1, edge0) // host - router1
        graph.addEdge(node0, node2, edge1) // host - router2
        graph.addEdge(node1, node2, edge2) // router2 - router1
        graph.addEdge(node2, node3, edge3) // router2 - server

        // Test nodes
        assertTrue(graph.areNeighbors(node0, node1))
        assertTrue(graph.areNeighbors(node0, node2))
        assertTrue(graph.areNeighbors(node1, node2))
        assertTrue(graph.areNeighbors(node2, node3))

        assertFalse(graph.areNeighbors(node3, node0))

        // Test edges
        assertEquals(graph.getEdge(node1, node0), edge0)
    }
}
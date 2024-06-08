package net_test

import com.example.net.BFS
import com.example.net.Edge
import com.example.net.NetGraph
import com.example.net.Player
import com.example.net.node.Host
import com.example.net.node.Router
import com.example.net.node.Server
import kotlin.test.Test
import kotlin.test.assertEquals

class BFSTest {

    private val player0 = Player(0, "player0")
    private val player1 = Player(1, "player1")
    private val player2 = Player(2, "player2")
    private val player3 = Player(3, "player3")

    @Test
    fun test1() {
        // Graph initialisation
        val graph = NetGraph()

        // Nodes
        //hosts
        val host0 = Host(0, player0)
        val host1 = Host(1, player1)
        val host2 = Host(2, player2)
        val host3 = Host(3, player3)
        val router4 = Router(4, 6) // router
        val router5 = Router(5, 5) // router
        val server6 = Server(6) // server

        // Edges
        val edge0 = Edge(0, 4)
        val edge1 = Edge(1, 2)
        val edge2 = Edge(2, 10)
        val edge3 = Edge(3, 10)
        val edge4 = Edge(4, 5)
        val edge5 = Edge(5, 4)
        val edge6 = Edge(6, 10)


        // Add elements to the structure
        graph.addNode(host0)
        graph.addNode(host1)
        graph.addNode(host2)
        graph.addNode(host3)
        graph.addNode(router4)
        graph.addNode(router5)
        graph.addNode(server6)

        graph.addEdge(host0, router4, edge0) // host0 - router4
        graph.addEdge(host1, router4, edge1) // host1 - router4
        graph.addEdge(host2, router4, edge6) // host2 - router4
        graph.addEdge(router4, router5, edge2) // router4 - router5
        graph.addEdge(host3, router5, edge3) // host3 - router5
        graph.addEdge(router4, server6, edge4) // router3 - server5
        graph.addEdge(router5, server6, edge5) // router4 - server5

        // create the nodeList for packet route from hosts to the server
        val nodeList0 = listOf(router4, router5, server6)
        host0.setRoute(nodeList0)

        val nodeList1 = listOf(router4, server6)
        host1.setRoute(nodeList1)

        val nodeList2 = listOf(router4, router5, server6)
        host2.setRoute(nodeList2)

        val nodeList3 = listOf(router5, server6)
        host3.setRoute(nodeList3)

        // --------------------------------------

        val engine = BFS(graph, server6)

        // ITERATION 1
        engine.run()

        // server
        assertEquals(0, server6.getPacketsReceived())

        // routers
        assertEquals(0, router4.getSpaceLeft())
        assertEquals(0, router5.getSpaceLeft())

        //hosts
        assertEquals(2, host0.getNumPacketsSent())
        assertEquals(2, host1.getNumPacketsSent())
        assertEquals(2, host2.getNumPacketsSent())

        assertEquals(5, host3.getNumPacketsSent())


        // ITERATION 2
        engine.run()

        // server
        assertEquals(6, server6.getPacketsReceived())

        //routers
        assertEquals(2, router4.getSpaceLeft())
        assertEquals(0, router5.getSpaceLeft())

        // hosts
        assertEquals(3, host0.getNumPacketsSent())
        assertEquals(3, host1.getNumPacketsSent())
        assertEquals(2, host2.getNumPacketsSent())

        assertEquals(7, host3.getNumPacketsSent())

    }
}
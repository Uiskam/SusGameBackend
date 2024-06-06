package net_test

import com.example.net.Edge
import com.example.net.NetGraph
import com.example.net.Player
import com.example.net.node.Host
import com.example.net.node.Router
import com.example.net.node.Server

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

public class RoutingTest {

    /**
     * Tests if the packet is properly created.
     */

    val player0 = Player(0, "player0")

    @Test
    fun creatingPacketTest() {

        // Nodes
        val host0 = Host(0, player0) //host
        val router1 = Router(1, 5) // router
        val server2 = Server(2) // server

        // create the nodeList for packet route ant set the route
        val nodeList = listOf(router1, server2)
        host0.setRoute(nodeList)

        // Try to get a packets from host
        val packet0 = host0.getPacket(router1)
        val packet1 = host0.getPacket(server2)

        // Host routes the packet to `router0`, therefore the remaining route should contain only `server2` object.
        assertEquals(packet0?.next(), server2)
        assertEquals(packet1?.next(), null)

        // Update packet route
        packet0?.popNext()
        assertEquals(packet0?.next(), server2)
    }

    @Test
    fun collectPacketFromOnePlayerTest() {
        // Graph initialisation
        val graph = NetGraph()

        // Nodes
        val host0 = Host(0, player0) //host
        val router1 = Router(1, 5) // router
        val router2 = Router(2, 5) // router
        val server3 = Server(3) // server

        // Edges
        val edge0 = Edge(0, 5)
        val edge1 = Edge(1, 5)
        val edge2 = Edge(2, 2)
        val edge3 = Edge(3, 5)


        // Add elements to the structure
        graph.addNode(host0)
        graph.addNode(router1)
        graph.addNode(router2)
        graph.addNode(server3)

        graph.addEdge(host0, router1, edge0) // host - router1
        graph.addEdge(host0, router2, edge1) // host - router2
        graph.addEdge(router1, router2, edge2) // router2 - router1
        graph.addEdge(router2, server3, edge3) // router2 - server

        // create the nodeList for packet route from host to the server
        val nodeList = listOf(router1, router2, server3)
        host0.setRoute(nodeList)

        //---------------------------------


        // Check if the node collects the packets properly

        router2.collectPackets()
        // Space left is supposed to be equal to 5 because there are no packets directed to `router2`.
        assertEquals(5, router2.getSpaceLeft())

        router1.collectPackets()
        // Space left is supposed to be equal to 0 because weight of `edge0` is 5 and bufferSize of `router1` is 5.
        assertEquals(0, router1.getSpaceLeft())

        router2.collectPackets()
        // spaceLeft in `router2` is supposed to be equal to 3 because weight of `edge0` is 2 and bufferSize of `router1` is 5.
        // spaceLeft in `router1` is supposed to be equal to 3 because `router2` takes 2 packets.
        assertEquals( 3, router2.getSpaceLeft())
        assertEquals(2, router1.getSpaceLeft())


    }
}



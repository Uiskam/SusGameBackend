package net

import node.Node
import edge.Edge
import engine.EdgePipeline
import kotlin.test.Test
import player.Player

class NodeTest {
    val player1: Player = Player(1, "player1")
    val player2: Player = Player(2, "player2")

    val players: HashSet<Player> = hashSetOf(player1, player2)

    @Test
    fun addToBuffer() {
        // create test node
        val node = Node(0, 8, players)

        // inputs from players
        node.newInputFor(player1, 3)
        node.newInputFor(player2, 6)

        // add inputs to buffer
        node.updateBuffer()

        // check the values
        assert(node.getPlayerBuffer(player1) == 3)
        assert(node.getPlayerBuffer(player2) == 5)
    }

    /**
     * Tests transferring data between nodes for one player.
     *
     */
    @Test
    fun sendFromNodeToNodeForOnePlayer() {
        // create nodes and edge
        val node1 = Node(0, 5, players)
        val node2 = Node(1, 5, players)

        val edge = Edge(0, 3)

        // add data to player buffer
        node1.newInputFor(player1, 4)
        node1.updateBuffer()

        // create pipeline and operation for transferring the data
        val pipeline = EdgePipeline(edge)

        pipeline.add(node1, node2, player1) //`player1` tries to send data from `node1` to `node2` through the `edge`

        // execute transferring pipeline
        pipeline.execute()

        // now the data from `player1` is supposed to be in `node2` input.
        // we have to update the buffer of `node2`
        node2.updateBuffer()

        // check the values
        assert(node1.getPlayerBuffer(player1) == 1)
        assert(node2.getPlayerBuffer(player1) == 3)

    }

    /**
     * Tests transferring data between nodes for two players in the same direction
     */
    @Test
    fun sendFromNodeToNodeForTwoPlayers() {
        // create nodes and edge
        val node1 = Node(0, 8, players)
        val node2 = Node(1, 6, players)

        val edge = Edge(0, 6)

        // add data to buffers for two players
        node1.newInputFor(player1, 4)
        node1.newInputFor(player2, 4)
        node1.updateBuffer()

        // create pipeline and operation for transferring data
        val pipeline = EdgePipeline(edge)
        pipeline.add(node1, node2, player1)
        pipeline.add(node1, node2, player2)  // players sending data in one direction from `node1` to `node2` through the `edge`

        // execute transferring pipeline
        pipeline.execute()

        // as the players tried to send more data than the size of an edge, both are supposed to send 3 packets
        // we have to update the buffer for `node2`
        node2.updateBuffer()


        // check the values for `node1`
        assert(node1.getPlayerBuffer(player1) == 1)
        assert(node1.getPlayerBuffer(player2) == 1)

        // check the values for `node2`
        assert(node2.getPlayerBuffer(player1) == 3)
        assert(node2.getPlayerBuffer(player2) == 3)
    }

    /**
     * Tests transferring data between nodes for two players in the opposite directions
     */
    @Test
    fun sendFromNodeToNodeForTwoPlayersOpposite() {
        // create nodes and edge
        val node1 = Node(0, 5, players)
        val node2 = Node(1, 5, players)

        val edge = Edge(0, 6)

        // add data to buffers for two players
        node1.newInputFor(player1, 4)
        node2.newInputFor(player2, 4)
        node1.updateBuffer()
        node2.updateBuffer()

        // create pipeline and operation for transferring data
        val pipeline = EdgePipeline(edge)

        // players sending data in the opposite directions through the `edge`
        pipeline.add(node1, node2, player1)  // `node1` to `node2`
        pipeline.add(node2, node1, player2)  // `node2` to `node1`

        // execute transferring pipeline
        pipeline.execute()

        // as the players tried to send more data than the size of an edge, both are supposed to send 3 packets
        // we have to update the buffer for `node2` and `node1`
        node2.updateBuffer()
        node1.updateBuffer()

        // check the values for `node1`
        assert(node1.getPlayerBuffer(player1) == 1)
        assert(node1.getPlayerBuffer(player2) == 3)

        // check the values for `node2`
        assert(node2.getPlayerBuffer(player1) == 3)
        assert(node2.getPlayerBuffer(player2) == 1)
    }


    /**
     * Tests the Round Robin algorithm balancing the input between the players
     * Transfers the data between nodes for two players.
     * Buffer is too small for the input.
     */
    @Test
    fun sendFromNodeToNodeForTwoPlayersOverflowingTheBuffer() {
        // create nodes and edge
        val node1 = Node(0, 10, players)
        val node2 = Node(1, 4, players)

        val edge = Edge(0, 10)

        // add data to buffers for two players
        node1.newInputFor(player1, 4)
        node1.newInputFor(player2, 4)
        node1.updateBuffer()

        // create pipeline and operation for transferring data
        val pipeline = EdgePipeline(edge)

        // players sending their whole data to `node2`
        pipeline.add(node1, node2, player1)  // `node1` to `node2`
        pipeline.add(node1, node2, player2)  // `node1` to `node2`

        // execute transferring pipeline
        pipeline.execute()

        // as the players sent more data than the buffer capacity, the buffer is supposed to balance their input
        // we have to update the buffer for `node2`
        node2.updateBuffer()

        // check the values for `node1` - edge was big enough for their data
        assert(node1.getPlayerBuffer(player1) == 0)
        assert(node1.getPlayerBuffer(player2) == 0)

        // check the values for `node2` - buffer is full
        assert(node2.getPlayerBuffer(player1) == 2)
        assert(node2.getPlayerBuffer(player2) == 2)
    }


}
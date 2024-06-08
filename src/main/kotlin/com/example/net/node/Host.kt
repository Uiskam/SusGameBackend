package com.example.net.node

import com.example.net.Packet
import com.example.net.Player

class Host(
    index: Int,
    private val player: Player
): Node(index) {

    private var route: List<Node>? = null
    private var firstNode: Node? = null // Node that the player is sending packets to

    private var numPacketsSent: Int = 0; // How many packets has the host already sent.

    /**
     * Stets a new route for generated packets.
     * Updates the packet route and the first node of the route.
     *
     * @param nodeList List of subsequent nodes starting from the first router after the player and ending with the server object.
     */
    public fun setRoute(nodeList: List<Node>) {
        if (nodeList.isNotEmpty()) {
            firstNode = nodeList[0]
            route = nodeList.subList(1, nodeList.size)
        } else {
            firstNode = null
            route = emptyList()
        }
    }

    /**
     * Host does not collect the packets - does nothing.
     */
    override fun collectPackets() {}

    /**
     * Creates a new packet if the node asking for the packet is the first in the route.
     *
     * @param node Node asking for the packet.
     * @return A new packet or null if the node is not first in the route or the route is empty.
     */
    override fun getPacket(node: Node): Packet? {
        return if (firstNode == node) {
            Packet(player, route!!).also { numPacketsSent++ }
        } else {
            null
        }
    }

    /**
     * Retrieves the number of packets the host has already sent.
     *
     * @return The number of packets sent from the start of simulation.
     */
    public fun getNumPacketsSent(): Int = numPacketsSent

}
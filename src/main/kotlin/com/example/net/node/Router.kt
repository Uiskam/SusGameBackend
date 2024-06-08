package com.example.net.node

import com.example.net.Edge
import com.example.net.Packet

/**
 * Represents the router object. Extends Sending.
 *
 * @param index
 * @param bufferSize Size of the buffer in the router.
 */
class Router (
    index: Int,
    private var bufferSize: Int
): Receiving(index) {

    // Buffer containing the waiting packets in a queue for every neighbour
    val buffer: HashMap<Node, ArrayDeque<Packet>> = hashMapOf()

    init {spaceLeft = bufferSize} // How much space is left in the whole buffer

    // Adds a neighbour both to the neighbours list and the buffer
    public override fun addNeighbour(node: Node, edge: Edge) {
        neighbors[node] = edge
        buffer[node] = ArrayDeque()
    }

    /**
     * Performs the buffer operation on the packet.
     * Updates the packet route.
     *
     * @param packet The packet received from the neighbor.
     */
    override fun pushPacket(packet: Packet) {
        val nextNode = packet.popNext() // Get the next node end delete it from packet route
        buffer[nextNode]!!.add(packet) // Add the packet to the buffer directing it to next node. We assume, that the nextNode is always in the neighbors of current node.
        spaceLeft--
    }

    /**
     * Retrieves the first packet directed to the specified node.
     * Updates the `spaceLeft` variable if the packet is sent.
     *
     * @param node Neighbor to get the packet from.
     * @return The first packet from the queue.
     */
    override fun getPacket(node: Node): Packet? = buffer[node]?.removeFirstOrNull()?.also { spaceLeft++ }


    /**
     * Retrieves how much space is left in the router buffer
     */
    public fun getSpaceLeft(): Int = spaceLeft


}
package com.example.net.node

import com.example.net.Edge
import com.example.net.Packet

/**
 * Represents the router object.
 *
 * @param index
 * @param bufferSize Size of the buffer in the router.
 */
class Router (
    index: Int,
    private var bufferSize: Int
): Node(index),  Sending{

    // Buffer containing the waiting packets in a queue for every neighbour
    val buffer: HashMap<Node, ArrayDeque<Packet>> = hashMapOf()

    private var spaceLeft: Int = bufferSize // How much space is left in the whole buffer

    // Pointer on last processed neighbour index for load balancing
    private var pointer: Int = 0

    // Adds a neighbour both to the neighbours list and the buffer
    public override fun addNeighbour(node: Node, edge: Edge) {
        neighbors[node] = edge
        buffer[node] = ArrayDeque()
        numNeighbours++
    }

    /**
     * Collects packets from the neighbours using Round Robin algorithm and adds them to the buffer.
     * Updates the packet route by popping the next node.
     */
    public override fun collectPackets() {
        val bandwidthLeft =
            neighbors.mapNotNull { it.value.getWeight() }.toMutableList()  // List representing how many packets can be sent using every edge in current iteration.
        val neighborList = neighbors.keys.toList()

        var noInputCounter = 0 // Counter checking how many neighbors did not send the packet. If the value grows to `numNeighbors` it means, that there are no packets directed to this node.

        while (spaceLeft > 0) {
            val neighbor = neighborList[pointer]

            noInputCounter++

            if (bandwidthLeft[pointer] > 0 && neighbor is Sending) { // Checks if the neighbor implements the sending interface

                val packet = neighborList[pointer].getPacket(this)

                if (packet != null) {
                    pushPacket(packet)
                    bandwidthLeft[pointer]-- // Update the number of packets that can be sent using this edge.
                    noInputCounter = 0 // Try another round over the neighbors looking for new packet
                }

            }

            if (noInputCounter == numNeighbours) break // No neighbors had packets directed to this node.

            pointer = (pointer + 1) % numNeighbours // Update the pointer to the next neighbor
        }

    }

    /**
     * Performs the buffer operation on the packet.
     * Updates the packet route.
     *
     * @param packet The packet received from the neighbor.
     */
    public override fun pushPacket(packet: Packet) {
        val nextNode = packet.popNext() // Get the next node end delete it from packet route
        buffer[nextNode]!!.addFirst(packet) // Add the packet to the buffer directing it to next node. We assume, that the nextNode is always in the neighbors of current node.
        spaceLeft--
    }

    /**
     * Retrieves the first packet directed to the specified node.
     *
     * @param node Neighbor to get the packet from.
     * @return The first packet from the queue.
     */
    override fun getPacket(node: Node): Packet? = buffer[node]?.removeFirstOrNull()


}
package com.example.net.node

import com.example.net.Packet

abstract class Receiving (
    index: Int
): Node(index) {

    // Pointer on last processed neighbour index for load balancing
    internal var pointer: Int = 0

    // How much space is left in the whole buffer
    internal var spaceLeft: Int = 0


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

            if (bandwidthLeft[pointer] > 0) { // Checks if the neighbor implements the sending interface
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
     * Abstract function accepting the packet.
     */
    abstract fun pushPacket(packet: Packet)

}
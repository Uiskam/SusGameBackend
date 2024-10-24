package edu.agh.susgame.back.net.node

import edu.agh.susgame.back.net.Packet

abstract class Receiving (
    index: Int
): Node(index) {

    // Pointer on last processed neighbour index for load balancing
    internal var pointer: Int = 0

    // How much space is left in the whole buffer.
    // Default value must be greater than 0 for the server to collect the packets properly.
    internal var spaceLeft: Int = 1


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

            noInputCounter++

            if (bandwidthLeft[pointer] > 0) {
                val packet = neighborList[pointer].getPacket(this)

                if (packet != null) {
                    pushPacket(packet)
                    bandwidthLeft[pointer]-- // Update the number of packets that can be sent using this edge.
                    println("Transported packets: ${neighbors[neighborList[pointer]]?.transportedPacketsThisTurn}")
                    noInputCounter = 0 // Try another round over the neighbors looking for new packet
                    neighbors[neighborList[pointer]]?.transportedPacketsThisTurn = neighbors[neighborList[pointer]]?.transportedPacketsThisTurn!! + 1
                    println("Transported packets: ${neighbors[neighborList[pointer]]?.transportedPacketsThisTurn}")
                    println()
                // Update the number of packets transported using this edge, for DTO purposes
                }

            }

            pointer = (pointer + 1) % countNeighbours() // Update the pointer to the next neighbor
            
            if ( noInputCounter == countNeighbours() ) break // No neighbors had packets directed to this node.
        }
    }

    /**
     * Abstract function accepting the packet.
     */
    abstract fun pushPacket(packet: Packet)

}
package edu.agh.susgame.back.net.node

import edu.agh.susgame.back.net.Edge
import edu.agh.susgame.back.net.Packet

/**
 * Represents the router object. Extends Sending.
 *
 * @param index
 * @param bufferSize Size of the buffer in the router.
 */
class Router(
    index: Int,
    private var bufferSize: Int
) : Receiving(index) {

    // Input buffer containing the packets received in a queue for every neighbor.
    private val inputBuffer: HashMap<Node, ArrayDeque<Packet>> = hashMapOf()

    // Buffer containing the waiting packets in a queue for every neighbour.
    private val buffer: HashMap<Node, ArrayDeque<Packet>> = hashMapOf()

    init {
        spaceLeft = bufferSize
    } // How much space is left in the whole buffer

    // Adds a neighbour both to the neighbours list and the buffer
    public override fun addNeighbour(node: Node, edge: Edge) {
        neighbors[node] = edge
        inputBuffer[node] = ArrayDeque<Packet>()
        buffer[node] = ArrayDeque<Packet>()
    }

    /**
     * Adds the packets from the inputBuffer to the queues in the buffer and clears the inputBuffer queues.
     */
    override fun updateBuffer() {
        for ((node, inputQueue) in inputBuffer) {
            buffer[node]?.addAll(inputQueue)
            inputQueue.clear()
        }
    }

    /**
     * Performs the buffer operation on the packet.
     * Updates the packet route.
     *
     * @param packet The packet received from the neighbor.
     */
    override fun pushPacket(packet: Packet) {
        val nextNode = packet.popNext() // Get the next node end delete it from packet route
        inputBuffer[nextNode]!!.add(packet) // Add the packet to the buffer directing it to next node. We assume, that the nextNode is always in the neighbors of current node.
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

    /**
     * Retrieves the all the packets in the buffers. Used for testing purposes.
     */
    public fun getPackets(): List<Packet> {
        val packets = mutableListOf<Packet>()
        for ((_, queue) in buffer) {
            packets.addAll(queue)
        }
        return packets
    }


}
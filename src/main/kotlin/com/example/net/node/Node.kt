package com.example.net.node

import com.example.net.Edge
import com.example.net.Packet


/**
 * Abstract class representing the node in the net.
 *
 * @param index Index of the node. Supposed to be unique.
 */
abstract class Node (
    internal val index: Int
) {

    // LinkedHashMap for a fixed order for use in Round Robin algorithm
    internal val neighbors: LinkedHashMap<Node, Edge> = linkedMapOf()

    internal var numNeighbours: Int = 0

    /**
     * Adds a new neighbor with the edge.
     *
     * @param node The new neighbor.
     * @param edge Edge connecting this node and the other one.
     */
    public open fun addNeighbour(node: Node, edge: Edge) {
        neighbors[node] = edge
        numNeighbours++
    }

    /**
     * Retrieves the neighbors of this node.
     *
     * @return HashSet of the nodes
     */
    public fun getNeighbors(): HashSet<Node> {
        return HashSet( neighbors.keys )
    }

    /**
     * Abstract function accepting the packet.
     */
    abstract fun collectPackets()

    /**
     * Abstract function accepting the packet.
     */
    abstract fun pushPacket(packet: Packet)

    /**
     * Abstract function returning the packet directed to this node.
     */
    abstract fun getPacket(node: Node): Packet?

    // Functions like hashCode() and equals() are overridden here
    override fun hashCode(): Int {
        return index
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Node) return false
        return this.index == other.index
    }

}
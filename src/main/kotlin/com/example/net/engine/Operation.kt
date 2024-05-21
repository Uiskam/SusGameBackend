package engine

import player.Player
import node.Node

/**
 * Represents an operation of sending a packet between two nodes in the graph.
 *
 * @property startNode The starting node of the operation.
 * @property endNode The ending node of the operation.
 * @property player The player performing the operation.
 */
class Operation (
    private val startNode: Node,
    private val endNode: Node,
    private val player: Player
) {

    /**
     * Runs the operation with the specified size of packet.
     * Updates the buffer of the starting node adding the packages sent before
     * and transfers the value to the ending node.
     *
     * @param value The value to transfer between nodes.
     */
    public fun run(value: Int) {
        // Update the buffer of the starting node
        startNode.updateBuffer()

        // Retrieve and delete the value from the buffer of the player at the starting node
        val valueSent: Int = startNode.getAndDeleteFrom(player, value)

        // Add the transferred value to the buffer of the player at the ending node
        endNode.newInputFor(player, valueSent)
    }
}
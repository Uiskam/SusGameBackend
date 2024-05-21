package engine

import player.Player
import edge.Edge
import node.Node

/**
 * Represents a pipeline for processing the operations of sending packets within one edge.
 * Calculates the value of bandwidth in case of multiple flows.
 *
 * @property edge The edge associated with the pipeline.
 */
class EdgePipeline (
    private val edge: Edge
) {

    // List of operations awaiting execution in the pipeline
    private var operations: ArrayList<Operation> = ArrayList()

    /**
     * Adds a new operation of sending a packet to the pipeline.
     *
     * @param startNode The node sending the packet.
     * @param endNode The node receiving the packet.
     * @param player The player performing the operation.
     */
    public fun add(startNode: Node, endNode: Node, player: Player) {
        val operation = Operation(startNode, endNode, player)
        operations.add(operation)
    }

    /**
     * Executes all operations in the pipeline.
     * Calculates the value of packet size depending on the number of players.
     * Cleans the list of operations after executing.
     */
    public fun execute() {
        // Determine the number of operations in the pipeline
        val numOperations: Int = operations.size

        // Packet value equal for each player
        val packetValue: Int = edge.getWeight() / numOperations

        // Execute each operation
        operations.forEach { operation ->
            operation.run(packetValue)
        }

        // Clear the list of operations after execution
        operations.clear()
    }

}
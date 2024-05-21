package player

import NetGraph
import edge.Edge
import node.Node

/**
 * Represents a route for the package for a specific player.
 * Route is based on nodes.
 *
 * @property netGraph The graph.
 * @property path The list of nodes representing the route.
 * @property player The player associated with the route.
 */
class Route public constructor(
    private val netGraph: NetGraph,
    private var path: ArrayList<Node>,
    private val player: Player
) {

    // The length of the route
    private var length: Int = 0

    // The pointer indicating the current position in the route
    private var pointer: Int = -1

    init {
        this.length = path.size
    }

    /**
     * Moves the pointer to the next node in the route.
     * If the end of the route is reached, resets the pointer.
     */
    public fun next() {
        if (pointer == length-1) {
            reset()
        }
        pointer++
    }

    /**
     * Resets the pointer to the beginning of the route.
     */
    public fun reset(){
        pointer = -1
    }

    /**
     * Retrieves the starting node of the current edge in the route.
     *
     * @return The starting node of the current edge.
     */
    public fun getStartNode(): Node {
        return path[pointer]
    }

    /**
     * Retrieves the ending node of the current edge in the route.
     *
     * @return The ending node of the current edge.
     */
    public fun getEndNode(): Node {
        return path[pointer + 1]
    }

    /**
     * Retrieves the current edge.
     *
     * @return The edge associated with the current position in the route.
     */
    public fun getCurrentEdge(): Edge {
        val start: Node = getStartNode()
        val end: Node = getEndNode()
        val edge: Edge = netGraph.getEdge(start, end)

        return edge
    }

    /**
     * Retrieves the player associated with the route.
     *
     * @return The player associated with the route.
     */
    public fun getPlayer(): Player {
        return player
    }
}
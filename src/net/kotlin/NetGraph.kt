import edge.Edge
import node.Node

/**
 * Represents the structure of the net as an undirected graph.
 */
class NetGraph {

    // The structure of the graph, mapping nodes to their adjacency lists
    private var structure: HashMap<Node, HashMap<Node, Edge>> = HashMap()

    // The set of all edges in the graph
    private var edges: HashSet<Edge> = HashSet()

    // The number of nodes in the graph
    private var size = 0

    /**
     * Adds a new node to the graph.
     *
     * @param node The node to add to the graph.
     */
    public fun addNode(node: Node) {
        structure[node] = HashMap()
        size++
    }

    /**
     * Connects two nodes in the graph with an edge.
     *
     * @param startNode The starting node of the edge.
     * @param endNode The ending node of the edge.
     * @param edge The edge to connect the nodes with.
     */
    public fun connect(startNode: Node, endNode: Node, edge: Edge) {
        structure[startNode]!![endNode] = edge
        structure[endNode]!![startNode] = edge

        edges.add(edge)
    }

    /**
     * Disconnects two nodes in the graph.
     *
     * @param startNode The starting node of the edge to disconnect.
     * @param endNode The ending node of the edge to disconnect.
     */
    public fun disconnect(startNode: Node, endNode: Node) {
        val edge: Edge = getEdge(startNode, endNode)
        edges.remove(edge)

        structure[startNode]!!.remove(endNode)
        structure[endNode]!!.remove(startNode)
    }

    /**
     * Retrieves the edge between two nodes in the graph.
     *
     * @param startNode The starting node of the edge.
     * @param endNode The ending node of the edge.
     * @return The edge between the specified nodes.
     */
    public fun getEdge(startNode: Node, endNode: Node): Edge {
        return structure[startNode]!![endNode]!!
    }

    /**
     * Retrieves the neighbors of a given node in graph.
     *
     * @param startNode The node to retrieve neighbors for.
     * @return The set of neighbor nodes.
     */
    public fun getNeighbours(startNode: Node): MutableSet<Node> {
        return structure[startNode]!!.keys
    }

    /**
     * Retrieves all edges in the graph.
     *
     * @return The set of all edges in the graph.
     */
    public fun getAllEdges(): HashSet<Edge> {
        return edges
    }
}

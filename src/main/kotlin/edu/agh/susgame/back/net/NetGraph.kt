package edu.agh.susgame.back.net

import edu.agh.susgame.back.net.node.Host
import edu.agh.susgame.back.net.node.Node
import edu.agh.susgame.back.net.node.Router
import edu.agh.susgame.back.net.node.Server
import java.util.concurrent.atomic.AtomicInteger

/**
 * Represents the structure of the net as an undirected graph
 */

class NetGraph {

    // Structure of the graph
    private val structure: HashMap<Node, HashMap<Node, Edge>> = HashMap()

    // Mutable list of edges
    private val edges: HashSet<Edge> = HashSet()

    // Map of hosts for DTO purposes
    private val hosts = HashMap<Int, Host>()

    // Map of routers for DTO purposes
    private val routers = HashMap<Int, Router>()

    // Map of servers for DTO purposes
    private val servers = HashMap<Int, Server>()



    /**
     * Resets the packet counters for all edges.
     * Sets the `transportedPacketsThisTurn` property of each edge to 0.
     */
    public fun resetEdges() {
        edges.forEach { it.transportedPacketsThisTurn = 0 }
    }

    /**
     * Adds a new node to the graph.
     *
     * @param node The node to add to the graph.
     */
    public fun addNode(node: Node) {
        structure[node] = HashMap()

        when (node) {
            is Host -> hosts[node.index] = node
            is Router -> routers[node.index] = node
            is Server -> servers[node.index] = node
            else -> {
                throw IllegalArgumentException("Node type not recognized $node")
            }
        }
    }

    public fun getHost(index: Int) = hosts[index]
    public fun getRouter(index: Int) = routers[index]
    public fun getServer(index: Int) = servers[index]

    public fun getHostsList() = hosts.values.toList()
    public fun getRoutersList() = routers.values.toList()
    public fun getServersList() = servers.values.toList()

    /**
     * Connects two nodes in the graph with an edge.
     * Adds the edge to the edge HashSet.
     * Adds new neighbours to the nodes.
     *
     * @param startNode The starting node of the edge.
     * @param endNode The ending node of the edge.
     * @param edge The edge to connect the nodes with.
     */
    public fun addEdge(startNode: Node, endNode: Node, edge: Edge) {
        // Connect the nodes
        structure[startNode]!![endNode] = edge
        structure[endNode]!![startNode] = edge

        // Add the edge to the edge HashSet
        edges.add(edge)

        // Add the neighbours
        startNode.addNeighbour(endNode, edge)
        endNode.addNeighbour(startNode, edge)
    }

    /**
     * Retrieves the neighbors of a given node in graph.
     *
     * @param node The node to retrieve neighbours of.
     * @return The HashSet of the neighbours. Null if node does not exist.
     */
    public fun getNeighbours(node: Node): HashSet<Node>? {
        return structure[node]?.keys?.let { HashSet(it) }
    }

    /**
     * Retrieves the edge between two nodes
     *
     * @param startNode Staring node of the edge.
     * @param endNode Ending node of the edge.
     * @return The edge between nodes. Null if edge does not exist.
     */
    public fun getEdge(startNode: Node, endNode: Node): Edge? {
        return structure[startNode]?.get(endNode)
    }

    /**
     * Retrieves all the nodes from the graph.
     *
     * @return HashSet of all nodes.
     */
    public fun getNodes(): HashSet<Node> = HashSet(structure.keys)

    /**
     * Retrieves all the edges from the graph.
     *
     * @return HashSet of all edges.
     */
    public fun getEdges(): HashSet<Edge> = edges

    /**
     * Checks if two nodes are neighbors in NetGraph structure.
     *
     * @param node1 First node.
     * @param node2 Second node.
     * @return Boolean value if the second node is in the neighbor list of the first node.
     */
    public fun areNeighbors(node1: Node, node2: Node): Boolean {
        val neighbors = getNeighbours(node1)
        return neighbors?.contains(node2) ?: false
    }

    /**
     * Updates the buffers of all routers.
     */
    public fun updateBuffers() {
        val nodes = getNodes()

        nodes.forEach { node -> node.updateBuffer() }
    }

}
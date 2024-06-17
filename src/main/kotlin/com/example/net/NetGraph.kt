package com.example.net

import com.example.net.node.Node
import com.example.net.node.Receiving
import com.example.net.node.Router

/**
 * Represents the structure of the net as an undirected graph
 */

class NetGraph {

    // Structure of the graph
    private val structure: HashMap<Node, HashMap<Node, Edge>> = HashMap()

    // Mutable list of edges
    private val edges: HashSet<Edge> = HashSet()

    /**
     * Adds a new node to the graph.
     *
     * @param node The node to add to the graph.
     */
    public fun addNode(node: Node) {
        structure[node] = HashMap()
    }

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
    public fun getNodes(): HashSet<Node> {
        return HashSet( structure.keys )
    }

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
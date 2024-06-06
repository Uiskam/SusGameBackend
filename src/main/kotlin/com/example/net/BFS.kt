package com.example.net

import com.example.net.node.Node
import com.example.net.node.Receiving
import com.example.net.node.Host


/**
 * The object performing BFS algorithm on the NetGraph.
 *
 * @param net The graph structure.
 * @param root Server node from the BFS tree starts from.
 */
class BFS (
    private val net: NetGraph,
    private val root: Host
) {

    // HashSet of all nodes
    val nodes: HashSet<Node> = net.getNodes()

    // Visited
    private val visited: HashMap<Node, Boolean> = nodes.associateWith { false } as HashMap<Node, Boolean>


    public fun runBFS() {

        val queue = ArrayDeque<Node>()
        queue.add(root)

        while(!queue.isEmpty()) {

            val currentNode = queue.removeFirst()
            visited[currentNode] = true

            if (!(currentNode is Receiving)) continue

            currentNode.collectPackets() // Collect packets from all neighbors

            // Perform rest of BFS algorithm
            val neighbours = net.getNeighbours(currentNode)

            if (neighbours != null) for (neighbour in neighbours) {
                if (!visited[neighbour]!!) queue.add(neighbour)
            }



        }


    }



}
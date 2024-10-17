package edu.agh.susgame.back.net.parser

import edu.agh.susgame.back.net.*
import edu.agh.susgame.back.net.node.*

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.File

@Serializable
data class NodeJson(val type: String, val bufferSize: Int? = null)

@Serializable
data class EdgeJson(val weight: Int, val from: Int, val to: Int)

@Serializable
data class GraphJson(val nodes: List<NodeJson>, val edges: List<EdgeJson>)

class GraphParser {
    /**
     * Parses a JSON file containing graph data and creates a NetGraph object.
     *
     * @param filePath The relative path to the JSON file within the `resources` directory.
     *                 For example, if the file is located at `src/main/resources/graph.json`,
     *                 we should pass "/graph.json".
     * @param players  A list of Player objects. Each Host node in the graph will be assigned
     *                 to exactly one player from this list in the order they appear. The number of players
     *                 must be at least equal to the number of Host nodes. (for now)
     * @return A NetGraph object constructed from the parsed JSON data.
     * @throws IllegalArgumentException If the file cannot be found or read, if there are not
     *                                  enough players for the Host nodes, or if the JSON data is invalid.
     */
    fun parseFromFile(filePath: String, players: List<Player>): NetGraph {
        // This does not work on linux
        //val fileUrl = javaClass.getResource(filePath)
        //    ?: throw IllegalArgumentException("File not found at path: $filePath")
        //val jsonString = File(fileUrl.toURI()).readText()
        val jsonString = File(filePath).readText()
        return parseGraph(jsonString, players)
    }

    private fun parseGraph(jsonString: String, players: List<Player>): NetGraph {
        val json = Json { ignoreUnknownKeys = true }
        val graphData = json.decodeFromString<GraphJson>(jsonString)

        val graph = NetGraph()
        val nodeMap = mutableMapOf<Int, Node>() // Map registering nodes under dynamically added indexes.
                                                // Edges connections are associated with the order of the
                                                // nodes in the json file.

        var playerIndex = 0 // Index of the player in the input player list used for associating every player
                            // with exactly one host.

       // Create Nodes and add to the NetGraph
        graphData.nodes.forEachIndexed { nodeIndex, nodeJson ->
            val node = when (nodeJson.type) {
                "Host" -> {
                    val player = players.getOrNull(playerIndex)
                        ?: throw IllegalArgumentException("Not enough players for hosts")
                    playerIndex++
                    Host(nodeIndex, player)
                }
                "Router" -> Router(nodeIndex, nodeJson.bufferSize!!)
                "Server" -> Server(nodeIndex)
                else -> throw IllegalArgumentException("Unknown node type")
            }
            nodeMap[nodeIndex] = node
            graph.addNode(node)
        }

        // Create Edges and connect Nodes
        graphData.edges.forEachIndexed { edgeIndex, edgeJson ->
            val fromNode = nodeMap[edgeJson.from] ?: throw IllegalArgumentException("Node not found")
            val toNode = nodeMap[edgeJson.to] ?: throw IllegalArgumentException("Node not found")
            val edge = Edge(edgeIndex, edgeJson.weight)
            graph.addEdge(fromNode, toNode, edge)
        }

        return graph
    }
}

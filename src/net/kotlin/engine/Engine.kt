package engine

import NetGraph
import edge.Edge
import node.Node
import player.Route

/**
 * Represents the engine that manages the simulation.
 *
 * @property netGraph The graph structure representing the game net.
 */

class Engine (
    private var netGraph: NetGraph
) {

    // List of routes for all players in the game
    private var routeList = ArrayList<Route>()

    // Pipelines associated with each edge in the graph, in current iteration
    private var pipelines: HashMap<Edge, EdgePipeline> = HashMap()

    // Initialize the engine
    init {
        // Create empty pipelines for each edge in the graph
        val edges: HashSet<Edge> = netGraph.getAllEdges()
        pipelines = edges.associateWith { edge -> EdgePipeline(edge) }.toMap() as HashMap<Edge, EdgePipeline>
    }

    /**
     * Adds a new route to the game engine.
     *
     * @param route The route to add.
     */
    public fun addRoute(route: Route) {
        routeList.add(route)
    }

    /**
     * Performs one iteration of the net simulation.
     * Adds the package data to the pipeline associated with a specific edge during current iteration.
     * After completing step in every route, executes the pipelines
     */
    private fun iteration() {
        for (route: Route in routeList) {
            val start: Node = route.getStartNode()
            val end: Node = route.getEndNode()
            val currentEdge: Edge = route.getCurrentEdge()

            val player = route.getPlayer()

            // Add player to the pipeline associated with the current edge
            pipelines[currentEdge]!!.add(start, end, player)
        }

        for (pipeline in pipelines.values) {
            pipeline.execute()
        }
    }

    /**
     * Runs the net simulation in a loop.
     */
    public fun run(){
        while (true) {
            iteration()
        }
    }
}

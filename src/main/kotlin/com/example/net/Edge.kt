package com.example.net

/**
 * Represents an edge in a graph.
 *
 * @property index The index of the edge (Supposed to be unique).
 * @property weight The weight of the edge.
 */

class Edge (
    private val index: Int,
    private var weight: Int
) {

    public fun getWeight() = weight

}
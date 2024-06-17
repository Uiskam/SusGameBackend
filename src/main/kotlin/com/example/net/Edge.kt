package com.example.net

/**
 * Represents an edge in a graph.
 *
 * @param index The index of the edge (Supposed to be unique).
 * @param weight The weight of the edge.
 */

class Edge (
    private val index: Int,
    private var weight: Int
) {

    public fun getWeight() = weight

}
package edge

/**
 * Represents an edge in a graph.
 *
 * @property index The index of the edge (Supposed to be unique).
 * @property weight The weight of the edge.
 */
class Edge(
    private val index: Int,
    private var weight: Int
) {
    /**
     * Retrieves the weight of the edge.
     *
     * @return The weight of the edge.
     */
    public fun getWeight(): Int {
        return weight
    }

    /**
     * Sets the weight of the edge.
     *
     * @param weight The new weight to be set.
     */
    public fun setWeight(weight: Int) {
        this.weight = weight
    }

    /**
     * Computes the hash code of the edge based on its index.
     *
     * @return The hash code of the edge.
     */
    override fun hashCode(): Int {
        return index
    }

    /**
     * Checks if this edge is equal to another object.
     *
     * @param other The object to compare with this edge.
     * @return `true` if the edges are equal, `false` otherwise.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Edge) return false
        return this.index == other.index
    }
}
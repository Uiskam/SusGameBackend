package node

import player.Player

/**
 * Represents a node in a graph of the net.
 *
 * @property index The index of the node (Supposed to be unique).
 * @property bufferSize The size of the buffer associated with the node.
 * @property players The set of all the players.
 */
class Node(
    private val index: Int,
    private val bufferSize: Int,
    private val players: HashSet<Player>
) {

    // Buffer associated with the node
    private val buffer: Buffer = Buffer(bufferSize, players)


    /**
     * Updates the buffer.
     */
    public fun updateBuffer() {
        buffer.update()
    }

    /**
     * Returns and removes a specific value from the buffer associated with a player.
     *
     * @param player The player whose buffer to operate on.
     * @param value The value to get and remove from the buffer.
     * @return The value retrieved from the buffer.
     */
    public fun getAndDeleteFrom(player: Player, value: Int): Int {
        return buffer.getAndDeleteFrom(player, value)
    }

    /**
     * Adds a new input value to the buffer associated with a player.
     *
     * @param player The player whose buffer to add the input to.
     * @param value The value to add to the buffer.
     */
    public fun newInputFor(player: Player, value: Int) {
        buffer.newInputFor(player, value)
    }

    /**
     * Returns the buffer value associated with a player.
     *
     * @param player The player whose buffer value to get.
     * @return The buffer value associated with the player, null if the player is not found.
     */
    public fun getPlayerBuffer(player: Player): Int? {
        return buffer.getPlayerBuffer(player)
    }

    /**
     * Computes the hash code of the node based on its index.
     *
     * @return The hash code of the node.
     */
    override fun hashCode(): Int {
        return index
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Node) return false
        return this.index == other.index
    }
}

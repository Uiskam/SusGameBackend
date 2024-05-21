package player

/**
 * Represents a player in the game.
 *
 * @property index The index of the player (Supposed to be unique).
 * @property name The name of the player.
 */
class Player(
    private val index: Int,
    private var name: String
) {
    // The score of the player
    private var score: Int = 0

    // The route of player package
    private var route: Route? = null

    /**
     * Computes the hash code of the player based on its index.
     *
     * @return The hash code of the player.
     */
    override fun hashCode(): Int {
        return index
    }

    /**
     * Checks if this player is equal to another object.
     *
     * @param other The object to compare with this player.
     * @return `true` if the players are equal, `false` otherwise.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Player) return false
        return this.index == other.index
    }
}
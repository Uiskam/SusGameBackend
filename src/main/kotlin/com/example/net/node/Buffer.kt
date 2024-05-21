package node

import player.Player

/**
 * Represents a buffer associated with a node in a graph.
 *
 * @property bufferSize The size of the buffer.
 * @property players The set of players associated with the buffer.
 */
class Buffer(
    private var bufferSize: Int,
    val players: HashSet<Player>
) {

    private var spaceLeft: Int = bufferSize // represents how much space is left in the buffer

    // Part of the buffer representing the values sent to the buffer from each player
    private var bufferInput: HashMap<Player, Int> = players.associateWith { 0 }.toMap() as HashMap<Player, Int>

    // Part of the buffer representing the real state of the buffer for each player
    private var bufferState: HashMap<Player, Int> = HashMap(bufferInput)

    /**
     * Adds a new value to the buffer of inputs associated for specific player.
     *
     * @param player The player whose buffer to add the input to.
     * @param value The value to add to the buffer.
     */
    public fun newInput(player: Player, value: Int) {
        bufferInput[player] = value
    }

    /**
     * Retrieves and removes a specific value from the buffer associated with a player.
     * Updates the variable representing free space in the buffer.
     *
     * @param player The player whose buffer to operate on.
     * @param value The value to retrieve and remove from the buffer.
     * @return The retrieved value from the buffer.
     */
    public fun getAndDelete(player: Player, value: Int): Int {
        val originalValue: Int = bufferState.getValue(player)
        val newValue: Int = originalValue - value

        bufferState[player] = maxOf(0, newValue)

        val valueSent: Int = minOf(originalValue, value)

        spaceLeft += valueSent

        return valueSent
    }

    /**
     * Updates the real buffer state based on the input values and buffer size and resets the buffer of inputs.
     */
    public fun update() {
        val maxInput: Int = minOf(bufferInput.values.maxOrNull() ?: 0, bufferSize)

        for (i in 0 until maxInput) {
            for (player in players) {

                if (bufferInput[player] != 0) {
                    if (spaceLeft == 0) break

                    bufferInput[player] = bufferInput[player]!! - 1
                    bufferState[player] = bufferState[player]!! + 1

                    spaceLeft -= 1
                }

            }
        }

        resetInput()
    }

    // Resets the input buffer to all zeros
    private fun resetInput() {
        bufferInput.replaceAll { _, _ -> 0 }
    }

    /**
     * Retrieves the buffer value associated with a specific player.
     *
     * @param player The player whose buffer value to retrieve.
     * @return The buffer value associated with the player, or null if the player is not found.
     */
    public fun getPlayerBuffer(player: Player): Int? {
        return bufferState[player]
    }
}
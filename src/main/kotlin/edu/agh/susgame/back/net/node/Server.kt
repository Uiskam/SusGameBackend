package edu.agh.susgame.back.net.node

import edu.agh.susgame.back.net.Packet
import edu.agh.susgame.dto.socket.server.ServerDTO

class Server (
    index: Int,
    coordinates: Pair<Int, Int> = Pair(0, 0)
): Receiving(index) {

    private var packetsReceived = 0 // Variable representing the progress of the game.

    /**
     * Updates the game progress by receiving the packet.
     */
    override fun pushPacket(packet: Packet) {
        packetsReceived++
    }

    /**
     * Server does not have any buffers - does nothing.
     */
    override fun updateBuffer() {}

    /**
     * Override function always returning null: Server does not send packets to any Node.
     */
    override fun getPacket(node: Node): Packet? {
        return null
    }

    /**
     * Retrieves the current state of the server.
     *
     * @return How many packets has the server already received.
     */
    public fun getPacketsReceived(): Int = packetsReceived

    public fun toDTO(): ServerDTO {
        return ServerDTO(
            id = index,
            packetsReceived = packetsReceived
        )
    }
}
package com.example.net.node

import com.example.net.Packet

class Host (
    index: Int
): Node(index), Sending {

    override fun collectPackets() {
        TODO("Not yet implemented")
    }

    override fun pushPacket(packet: Packet) {
        TODO("Not yet implemented")
    }

    override fun getPacket(node: Node): Packet? {
        TODO("Not yet implemented")
    }
}
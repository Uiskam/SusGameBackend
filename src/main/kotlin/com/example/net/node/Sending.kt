package com.example.net.node
import com.example.net.Packet


/**
 * Interface representing features of nodes performing actions of sending packets.
 * Properties and behavior shared by Host and Router.
 */
interface Sending  {
    fun getPacket(node: Node): Packet?
}
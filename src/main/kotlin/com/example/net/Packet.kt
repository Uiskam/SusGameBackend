package com.example.net

import com.example.net.node.Node

class Packet (
    private val player: Player
){

    private val route: ArrayDeque<Node> = ArrayDeque()

    public fun popNext(): Node = route.removeFirst()
    public fun next(): Node? = route.firstOrNull()

}
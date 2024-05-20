package edu.agh.susgame.back

import edu.agh.susgame.back.plugins.configureRouting
import edu.agh.susgame.back.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}

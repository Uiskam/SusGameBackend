package edu.agh.susgame.back

import edu.agh.susgame.back.plugins.configureRouting
import edu.agh.susgame.back.plugins.configureSerialization
import edu.agh.susgame.back.rest.games.ErrorObj
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(StatusPages) {
        exception<NumberFormatException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ErrorObj("${cause.message}. The input param is not a valid number"))
        }
        exception<IllegalArgumentException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ErrorObj("${cause.message}. The input param is not valid"))
        }
    }
    configureSerialization()
    configureRouting()
}

package com.example.plugins

import com.example.routes.gameRouting
import io.ktor.server.application.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import java.time.Duration

fun Application.configureRouting() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        gameRouting()
        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")
        openAPI(path="openapi", swaggerFile = "openapi/documentation.yaml")
    }
}

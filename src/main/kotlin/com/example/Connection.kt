package com.example

import io.ktor.websocket.*
import java.util.concurrent.atomic.*

class Connection(val session: DefaultWebSocketSession)
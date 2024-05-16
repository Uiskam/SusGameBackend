package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Game(val name:String)

val gameStorage = mutableListOf<Game>()
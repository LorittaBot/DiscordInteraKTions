package net.perfectdreams.discordinteraktions.entities

interface Message {
    val content: String

    suspend fun editMessage(content: String)
}
package net.perfectdreams.discordinteraktions.entities

class OriginalMessage(override val content: String) : Message {
    override suspend fun editMessage(content: String) {
        TODO()
    }
}
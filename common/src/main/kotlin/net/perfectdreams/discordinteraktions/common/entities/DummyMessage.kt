package net.perfectdreams.discordinteraktions.common.entities

import net.perfectdreams.discordinteraktions.common.utils.InteractionMessage

class DummyMessage : Message {
    override val content: String
        get() = TODO("Not yet implemented")

    override suspend fun editMessage(message: InteractionMessage): Message {
        TODO("Not yet implemented")
    }
}
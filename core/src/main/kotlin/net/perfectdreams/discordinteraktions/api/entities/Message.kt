package net.perfectdreams.discordinteraktions.api.entities

import net.perfectdreams.discordinteraktions.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.utils.MessageBuilder
import net.perfectdreams.discordinteraktions.utils.buildMessage

interface Message {
    val content: String

    suspend fun editMessage(block: MessageBuilder.() -> (Unit)): Message {
        val message = buildMessage(block)
        return editMessage(message)
    }

    suspend fun editMessage(message: InteractionMessage): Message
}
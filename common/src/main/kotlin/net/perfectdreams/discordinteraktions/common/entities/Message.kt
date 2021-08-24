package net.perfectdreams.discordinteraktions.common.entities

import net.perfectdreams.discordinteraktions.api.entities.Snowflake
import net.perfectdreams.discordinteraktions.common.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.common.utils.MessageBuilder
import net.perfectdreams.discordinteraktions.common.utils.buildMessage

interface Message {
    val id: Snowflake
    val content: String

    suspend fun editMessage(block: MessageBuilder.() -> (Unit)): Message {
        val message = buildMessage(block)
        return editMessage(message)
    }

    suspend fun editMessage(message: InteractionMessage): Message
}
package net.perfectdreams.discordinteraktions.common.entities.messages

import net.perfectdreams.discordinteraktions.common.utils.MessageBuilder

interface PublicThinkingMessage : ThinkingMessage {
    suspend fun editMessage(block: MessageBuilder.() -> (Unit)): PublicMessage
}
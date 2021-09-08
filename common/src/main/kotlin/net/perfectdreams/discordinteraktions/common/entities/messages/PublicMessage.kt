package net.perfectdreams.discordinteraktions.common.entities.messages

import net.perfectdreams.discordinteraktions.common.utils.MessageCreateBuilder

interface PublicMessage : Message {
    suspend fun editMessage(block: MessageCreateBuilder.() -> (Unit)): PublicMessage
}
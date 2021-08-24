package net.perfectdreams.discordinteraktions.common.entities

import net.perfectdreams.discordinteraktions.common.utils.MessageBuilder

interface PublicMessage : Message {
    suspend fun editMessage(block: MessageBuilder.() -> (Unit)): PublicMessage
}
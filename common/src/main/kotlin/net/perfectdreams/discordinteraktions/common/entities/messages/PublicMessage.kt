package net.perfectdreams.discordinteraktions.common.entities.messages

import net.perfectdreams.discordinteraktions.common.builder.message.modify.PersistentMessageModifyBuilder

interface PublicMessage : Message {
    suspend fun editMessage(block: PersistentMessageModifyBuilder.() -> (Unit)): PublicMessage
    suspend fun editMessage(message: PersistentMessageModifyBuilder): PublicMessage
}
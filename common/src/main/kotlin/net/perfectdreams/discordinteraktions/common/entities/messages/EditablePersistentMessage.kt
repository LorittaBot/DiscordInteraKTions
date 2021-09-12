package net.perfectdreams.discordinteraktions.common.entities.messages

import net.perfectdreams.discordinteraktions.common.builder.message.modify.PersistentMessageModifyBuilder

interface EditablePersistentMessage : Message {
    suspend fun editMessage(block: PersistentMessageModifyBuilder.() -> (Unit)): EditablePersistentMessage
    suspend fun editMessage(message: PersistentMessageModifyBuilder): EditablePersistentMessage
}
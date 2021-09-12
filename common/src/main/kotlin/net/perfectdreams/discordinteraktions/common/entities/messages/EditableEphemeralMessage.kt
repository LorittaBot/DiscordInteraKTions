package net.perfectdreams.discordinteraktions.common.entities.messages

import net.perfectdreams.discordinteraktions.common.builder.message.modify.EphemeralMessageModifyBuilder

interface EditableEphemeralMessage : Message {
    suspend fun editMessage(block: EphemeralMessageModifyBuilder.() -> (Unit)): EditableEphemeralMessage
    suspend fun editMessage(message: EphemeralMessageModifyBuilder): EditableEphemeralMessage
}
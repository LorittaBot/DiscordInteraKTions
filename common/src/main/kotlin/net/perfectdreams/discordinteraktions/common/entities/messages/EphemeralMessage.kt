package net.perfectdreams.discordinteraktions.common.entities.messages

import net.perfectdreams.discordinteraktions.common.builder.message.modify.EphemeralMessageModifyBuilder

interface EphemeralMessage : Message {
    suspend fun editMessage(block: EphemeralMessageModifyBuilder.() -> (Unit)): EphemeralMessage
    suspend fun editMessage(message: EphemeralMessageModifyBuilder): EphemeralMessage
}
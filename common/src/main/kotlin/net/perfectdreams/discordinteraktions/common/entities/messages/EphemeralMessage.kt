package net.perfectdreams.discordinteraktions.common.entities.messages

import net.perfectdreams.discordinteraktions.common.utils.EphemeralMessageCreateBuilder

interface EphemeralMessage : Message {
    suspend fun editMessage(block: EphemeralMessageCreateBuilder.() -> (Unit)): EphemeralMessage
}
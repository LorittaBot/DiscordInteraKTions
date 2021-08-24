package net.perfectdreams.discordinteraktions.common.entities.messages

import net.perfectdreams.discordinteraktions.common.utils.EphemeralMessageBuilder

interface EphemeralThinkingMessage : ThinkingMessage {
    suspend fun editMessage(block: EphemeralMessageBuilder.() -> (Unit)): EphemeralMessage
}
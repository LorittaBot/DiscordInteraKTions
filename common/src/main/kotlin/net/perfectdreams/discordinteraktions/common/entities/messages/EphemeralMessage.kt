package net.perfectdreams.discordinteraktions.common.entities.messages

import net.perfectdreams.discordinteraktions.common.builder.message.modify.EphemeralInteractionOrFollowupMessageModifyBuilder

interface EphemeralMessage : Message {
    suspend fun editMessage(block: EphemeralInteractionOrFollowupMessageModifyBuilder.() -> (Unit)): EphemeralMessage
}
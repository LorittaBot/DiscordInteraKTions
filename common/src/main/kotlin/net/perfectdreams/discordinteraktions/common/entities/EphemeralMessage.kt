package net.perfectdreams.discordinteraktions.common.entities

import net.perfectdreams.discordinteraktions.common.utils.EphemeralMessageBuilder
import net.perfectdreams.discordinteraktions.common.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.common.utils.buildEphemeralMessage
import net.perfectdreams.discordinteraktions.common.utils.buildMessage

interface EphemeralMessage : Message {
    suspend fun editMessage(block: EphemeralMessageBuilder.() -> (Unit)): EphemeralMessage
}
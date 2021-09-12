package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.DiscordMessage
import net.perfectdreams.discordinteraktions.common.builder.message.modify.EphemeralInteractionOrFollowupMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.modify.EphemeralMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.entities.messages.EphemeralMessage

open class KordEphemeralMessage(val handle: DiscordMessage) : EphemeralMessage {
    override val id = handle.id
    override val content by handle::content

    override suspend fun editMessage(block: EphemeralMessageModifyBuilder.() -> Unit): EphemeralMessage {
        TODO("Not yet implemented")
    }

    override suspend fun editMessage(message: EphemeralMessageModifyBuilder): EphemeralMessage {
        TODO("Not yet implemented")
    }
}
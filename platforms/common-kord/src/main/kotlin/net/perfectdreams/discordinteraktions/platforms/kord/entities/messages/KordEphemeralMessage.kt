package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.DiscordMessage
import net.perfectdreams.discordinteraktions.common.builder.message.modify.EphemeralInteractionOrFollowupMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.modify.EphemeralMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.entities.messages.EphemeralMessage

open class KordEphemeralMessage(val handle: DiscordMessage) : EphemeralMessage {
    override val id = handle.id
    override val content by handle::content
}
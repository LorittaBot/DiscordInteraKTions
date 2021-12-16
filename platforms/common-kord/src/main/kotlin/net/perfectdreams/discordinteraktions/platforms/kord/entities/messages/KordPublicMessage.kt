package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.DiscordMessage
import kotlinx.serialization.Serializable
import net.perfectdreams.discordinteraktions.common.entities.messages.PublicMessage

@Serializable
open class KordPublicMessage(val handle: DiscordMessage) : PublicMessage {
    override val id = handle.id
    override val content by handle::content
}
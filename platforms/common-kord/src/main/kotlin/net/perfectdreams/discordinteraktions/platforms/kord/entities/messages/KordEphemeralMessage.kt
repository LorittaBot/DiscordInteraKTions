package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.DiscordMessage
import net.perfectdreams.discordinteraktions.common.entities.messages.EphemeralMessage
import net.perfectdreams.discordinteraktions.common.utils.EphemeralMessageBuilder
import net.perfectdreams.discordinteraktions.platforms.kord.utils.toDiscordInteraKTionsSnowflake

open class KordEphemeralMessage(val handle: DiscordMessage) : EphemeralMessage {
    override val id = handle.id.toDiscordInteraKTionsSnowflake()
    override val content by handle::content

    override suspend fun editMessage(block: EphemeralMessageBuilder.() -> Unit): EphemeralMessage {
        TODO("Not yet implemented")
    }
}
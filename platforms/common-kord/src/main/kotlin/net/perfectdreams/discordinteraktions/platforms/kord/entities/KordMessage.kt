package net.perfectdreams.discordinteraktions.platforms.kord.entities

import dev.kord.common.entity.DiscordMessage
import net.perfectdreams.discordinteraktions.common.entities.Message
import net.perfectdreams.discordinteraktions.platforms.kord.utils.toDiscordInteraKTionsSnowflake

open class KordMessage(val handle: DiscordMessage) : Message {
    override val id = handle.id.toDiscordInteraKTionsSnowflake()
    override val content by handle::content
}
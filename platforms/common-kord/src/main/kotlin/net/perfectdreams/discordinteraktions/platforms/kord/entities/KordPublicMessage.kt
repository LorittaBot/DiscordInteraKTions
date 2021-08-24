package net.perfectdreams.discordinteraktions.platforms.kord.entities

import dev.kord.common.entity.DiscordMessage
import net.perfectdreams.discordinteraktions.common.entities.Message
import net.perfectdreams.discordinteraktions.common.entities.PublicMessage
import net.perfectdreams.discordinteraktions.common.utils.MessageBuilder
import net.perfectdreams.discordinteraktions.platforms.kord.utils.toDiscordInteraKTionsSnowflake

open class KordPublicMessage(val handle: DiscordMessage) : PublicMessage {
    override val id = handle.id.toDiscordInteraKTionsSnowflake()
    override val content by handle::content

    override suspend fun editMessage(block: MessageBuilder.() -> Unit): PublicMessage {
        TODO("Not yet implemented")
    }
}
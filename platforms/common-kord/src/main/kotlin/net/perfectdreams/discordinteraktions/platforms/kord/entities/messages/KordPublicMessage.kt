package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.DiscordMessage
import net.perfectdreams.discordinteraktions.common.entities.messages.PublicMessage
import net.perfectdreams.discordinteraktions.common.utils.MessageCreateBuilder
import net.perfectdreams.discordinteraktions.platforms.kord.utils.toDiscordInteraKTionsSnowflake

open class KordPublicMessage(val handle: DiscordMessage) : PublicMessage {
    override val id = handle.id.toDiscordInteraKTionsSnowflake()
    override val content by handle::content

    override suspend fun editMessage(block: MessageCreateBuilder.() -> Unit): PublicMessage {
        TODO("Not yet implemented")
    }
}
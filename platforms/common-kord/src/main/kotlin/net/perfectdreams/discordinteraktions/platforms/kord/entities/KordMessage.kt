package net.perfectdreams.discordinteraktions.platforms.kord.entities

import dev.kord.common.entity.DiscordMessage
import net.perfectdreams.discordinteraktions.common.entities.Message
import net.perfectdreams.discordinteraktions.common.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.platforms.kord.utils.toDiscordInteraKTionsSnowflake

class KordMessage(val handle: DiscordMessage) : Message {
    override val id = handle.id.toDiscordInteraKTionsSnowflake()
    override val content by handle::content

    override suspend fun editMessage(message: InteractionMessage): Message {
        TODO("Not yet implemented")
    }
}
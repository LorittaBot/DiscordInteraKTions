package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.DiscordMessage
import net.perfectdreams.discordinteraktions.common.builder.message.modify.PersistentMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.modify.PublicInteractionOrFollowupMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.entities.messages.PublicMessage

open class KordPublicMessage(val handle: DiscordMessage) : PublicMessage {
    override val id = handle.id
    override val content by handle::content

    override suspend fun editMessage(block: PersistentMessageModifyBuilder.() -> Unit): PublicMessage {
        TODO("Not yet implemented")
    }

    override suspend fun editMessage(message: PersistentMessageModifyBuilder): PublicMessage {
        TODO("Not yet implemented")
    }
}
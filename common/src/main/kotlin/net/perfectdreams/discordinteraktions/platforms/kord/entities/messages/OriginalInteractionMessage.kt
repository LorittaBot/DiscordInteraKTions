package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.DiscordAttachment
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Member
import dev.kord.core.entity.User
import kotlinx.datetime.Instant
import net.perfectdreams.discordinteraktions.common.entities.messages.Message

open class OriginalInteractionMessage : Message {
    override val id: Snowflake
        get() = fail()
    override val channelId: Snowflake
        get() = fail()
    override val guildId: Snowflake?
        get() = fail()
    override val author: User
        get() = fail()
    override val member: Member?
        get() = fail()
    override val content: String
        get() = fail()
    override val timestamp: Instant
        get() = fail()
    override val editedTimestamp: Instant?
        get() = fail()
    override val attachments: List<DiscordAttachment>
        get() = fail()

    private fun fail(): Nothing = error("Original Interaction Messages do not have any data associated with it!")
}
package net.perfectdreams.discordinteraktions.common.entities.messages

import dev.kord.common.entity.DiscordAttachment
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Member
import dev.kord.core.entity.User
import kotlinx.datetime.Instant

interface Message {
    val id: Snowflake
    val channelId: Snowflake
    val guildId: Snowflake?
    val author: User
    val member: Member?
    val content: String?
    val timestamp: Instant
    val editedTimestamp: Instant?
    val attachments: List<DiscordAttachment>
}
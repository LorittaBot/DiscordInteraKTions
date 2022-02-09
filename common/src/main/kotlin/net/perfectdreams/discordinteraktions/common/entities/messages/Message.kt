package net.perfectdreams.discordinteraktions.common.entities.messages

import dev.kord.common.entity.DiscordAttachment
import dev.kord.common.entity.Snowflake
import kotlinx.datetime.Instant
import net.perfectdreams.discordinteraktions.common.entities.InteractionMember
import net.perfectdreams.discordinteraktions.common.entities.Member
import net.perfectdreams.discordinteraktions.common.entities.User

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
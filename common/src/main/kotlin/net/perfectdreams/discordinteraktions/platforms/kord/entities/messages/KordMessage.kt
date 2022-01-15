package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
import kotlinx.datetime.Instant
import net.perfectdreams.discordinteraktions.api.entities.Member
import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.entities.messages.Message
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordMember
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordUser

open class KordMessage(val data: DiscordMessage) : Message {
    override val id by data::id
    override val channelId by data::channelId
    override val guildId: Snowflake?
        get() = data.guildId.value
    override val author: User
        get() = KordUser(data.author)
    override val member: Member?
        get() = data.member.value?.let { KordMember(it, author) }
    override val content by data::content
    override val timestamp: Instant
        get() = Instant.parse(data.timestamp)
    override val editedTimestamp: Instant?
        get() = data.editedTimestamp?.let { Instant.parse(it) }
    override val attachments by data::attachments
}
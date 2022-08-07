package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.cache.data.MemberData
import dev.kord.core.cache.data.UserData
import dev.kord.core.entity.Member
import dev.kord.core.entity.User
import net.perfectdreams.discordinteraktions.common.entities.messages.Message

open class KordMessage(val kord: Kord, val data: DiscordMessage) : Message {
    override val id by data::id
    override val channelId by data::channelId
    override val guildId: Snowflake?
        get() = data.guildId.value
    override val author: User
        get() = User(UserData.from(data.author), kord)
    override val member: Member?
        get() = data.member.value?.let {
            // I don't think the guildId is null if the member object is present
            Member(MemberData.from(author.id, guildId!!, it), author.data, kord)
        }
    override val content by data::content
    override val timestamp by data::timestamp
    override val editedTimestamp by data::editedTimestamp
    override val attachments by data::attachments
}
package net.perfectdreams.discordinteraktions.platforms.kord.entities

import dev.kord.common.entity.DiscordGuildMember
import dev.kord.common.entity.Snowflake
import kotlinx.datetime.Instant
import net.perfectdreams.discordinteraktions.common.entities.Icon
import net.perfectdreams.discordinteraktions.common.entities.Member
import net.perfectdreams.discordinteraktions.common.entities.User

// This is the same thing as KordMember, however an Interaction guild member does not have a deaf or mute flag
class KordMember(
    private val guildId: Snowflake,
    val handle: DiscordGuildMember,
    override val user: User // The user object is here too because sometimes the handle user value may be null!
) : Member {
    override val nick: String?
        get() = handle.nick.value
    override val roles by handle::roles
    override val joinedAt = handle.joinedAt
    override val premiumSince = handle.premiumSince.value
    override val pending: Boolean
        get() = handle.pending.discordBoolean
    override val avatarHash = handle.avatar.value
    override val avatar = avatarHash?.let {
        Icon.MemberAvatar(guildId, user.id, it)
    }
    override val communicationDisabledUntil: Instant?
        get() = handle.communicationDisabledUntil.value
}
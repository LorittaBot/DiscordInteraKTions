package net.perfectdreams.discordinteraktions.platforms.kord.entities

import dev.kord.common.entity.DiscordInteractionGuildMember
import dev.kord.common.entity.Snowflake
import kotlinx.datetime.Instant
import net.perfectdreams.discordinteraktions.common.entities.Icon
import net.perfectdreams.discordinteraktions.common.entities.InteractionMember
import net.perfectdreams.discordinteraktions.common.entities.User
import net.perfectdreams.discordinteraktions.common.entities.UserAvatar

// This is the same thing as KordMember, however an Interaction guild member does not have a deaf or mute flag
class KordInteractionMember(
    private val guildId: Snowflake,
    val handle: DiscordInteractionGuildMember,
    override val user: User // The user object is here too because sometimes the handle user value may be null!
) : InteractionMember {
    override val nick: String?
        get() = handle.nick.value
    override val roles by handle::roles
    override val joinedAt = Instant.parse(handle.joinedAt)
    override val premiumSince = handle.premiumSince.value?.let { Instant.parse(it) }
    override val permissions by handle::permissions
    override val pending: Boolean
        get() = handle.pending.discordBoolean
    override val avatar = handle.avatar.value?.let {
        Icon.MemberAvatar(guildId, user.id, it)
    }
    override val communicationDisabledUntil: Instant?
        get() = handle.communicationDisabledUntil.value
}
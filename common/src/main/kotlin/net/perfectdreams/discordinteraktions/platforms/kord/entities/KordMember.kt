package net.perfectdreams.discordinteraktions.platforms.kord.entities

import dev.kord.common.entity.DiscordGuildMember
import dev.kord.common.entity.DiscordInteractionGuildMember
import kotlinx.datetime.Instant
import net.perfectdreams.discordinteraktions.common.entities.InteractionMember
import net.perfectdreams.discordinteraktions.common.entities.Member
import net.perfectdreams.discordinteraktions.common.entities.User
import net.perfectdreams.discordinteraktions.common.entities.UserAvatar

// This is the same thing as KordMember, however an Interaction guild member does not have a deaf or mute flag
class KordMember(
    val handle: DiscordGuildMember,
    override val user: User // The user object is here too because sometimes the handle user value may be null!
) : Member {
    override val nick: String?
        get() = handle.nick.value
    override val roles by handle::roles
    override val joinedAt = Instant.parse(handle.joinedAt)
    override val premiumSince = handle.premiumSince.value?.let { Instant.parse(it) }
    override val pending: Boolean
        get() = handle.pending.discordBoolean
    override val avatar = handle.avatar.value?.let {
        UserAvatar(
            user.id.value,
            user.discriminator.toInt(),
            it
        )
    }
    override val communicationDisabledUntil: Instant?
        get() = handle.communicationDisabledUntil.value
}
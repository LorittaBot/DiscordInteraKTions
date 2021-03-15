package net.perfectdreams.discordinteraktions.internal.entities

import dev.kord.common.entity.DiscordInteractionGuildMember
import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.Member
import net.perfectdreams.discordinteraktions.api.entities.User

class KordMember(val handle: DiscordInteractionGuildMember) : Member {
    val kordUser = KordUser(handle.user.value ?: throw IllegalArgumentException("Missing user!"))

    override val user: User
        get() = kordUser
    override val roles: List<Snowflake>
        get() = handle.roles
}
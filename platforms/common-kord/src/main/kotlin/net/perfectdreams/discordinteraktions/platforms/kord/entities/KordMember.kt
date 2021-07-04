package net.perfectdreams.discordinteraktions.platforms.kord.entities

import dev.kord.common.entity.DiscordInteractionGuildMember
import net.perfectdreams.discordinteraktions.api.entities.Member
import net.perfectdreams.discordinteraktions.api.entities.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.User

class KordMember(val handle: DiscordInteractionGuildMember) : Member {
    val kordUser = KordUser(handle.user.value ?: throw IllegalArgumentException("Missing user!"))

    override val user: User
        get() = kordUser
    override val roles: List<Snowflake>
        get() = handle.roles.map { Snowflake(it.value) }
}
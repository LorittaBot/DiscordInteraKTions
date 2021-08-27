package net.perfectdreams.discordinteraktions.platform.jda.entities

import net.perfectdreams.discordinteraktions.api.entities.Member
import net.perfectdreams.discordinteraktions.api.entities.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.api.entities.UserAvatar

class JDAMember(private val member: net.dv8tion.jda.api.entities.Member) : Member {
    override val user: User
        get() = JDAUser(member.user)
    override val roles: List<Snowflake>
        get() = member.roles.map { Snowflake(it.idLong) }
}
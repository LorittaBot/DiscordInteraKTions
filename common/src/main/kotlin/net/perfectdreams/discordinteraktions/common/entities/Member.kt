package net.perfectdreams.discordinteraktions.common.entities

import dev.kord.common.entity.Snowflake

interface Member {
    val user: User
    val roles: List<Snowflake>
}
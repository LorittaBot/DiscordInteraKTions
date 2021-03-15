package net.perfectdreams.discordinteraktions.api.entities

import dev.kord.common.entity.Snowflake

interface User {
    val id: Snowflake
    val username: String
    val discriminator: String
    val avatar: String?
    val bot: Boolean
}

inline val User.idLong get() = id.value

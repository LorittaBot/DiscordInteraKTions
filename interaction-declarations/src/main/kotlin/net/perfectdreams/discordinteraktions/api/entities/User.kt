package net.perfectdreams.discordinteraktions.api.entities

import dev.kord.common.entity.Snowflake

interface User {
    val id: Snowflake
    val idLong: Long
        get() = id.value
    val username: String
    val discriminator: String
    val avatar: String?
    val bot: Boolean
}
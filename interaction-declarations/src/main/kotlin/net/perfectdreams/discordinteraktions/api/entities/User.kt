package net.perfectdreams.discordinteraktions.api.entities

import dev.kord.common.entity.Snowflake

interface User {
    val id: Snowflake
    val user: String
    val discriminator: String
    val avatar: UserAvatar
    val bot: Boolean
}

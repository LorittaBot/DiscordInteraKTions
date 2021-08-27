package net.perfectdreams.discordinteraktions.api.entities

interface User {
    val id: Snowflake
    val name: String
    val discriminator: String
    val avatar: UserAvatar
    val bot: Boolean
}

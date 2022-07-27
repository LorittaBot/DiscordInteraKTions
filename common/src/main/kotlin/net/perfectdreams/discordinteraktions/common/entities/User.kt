package net.perfectdreams.discordinteraktions.common.entities

import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.UserFlag

interface User : Mentionable {
    val id: Snowflake
    val name: String
    val discriminator: String
    val avatarHash: String?
    val avatar: Icon
    val bot: Boolean
    val publicFlags: List<UserFlag>
}
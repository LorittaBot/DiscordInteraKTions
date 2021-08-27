package net.perfectdreams.discordinteraktions.common.utils

import net.perfectdreams.discordinteraktions.api.entities.Snowflake

data class AllowedMentions(
    val users: List<Snowflake>,
    val roles: List<Snowflake>,
    val repliedUser: Boolean? = null
)
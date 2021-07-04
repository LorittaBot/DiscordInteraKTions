package net.perfectdreams.discordinteraktions.platforms.kord.utils

import net.perfectdreams.discordinteraktions.api.entities.Snowflake

fun Snowflake.toKordSnowflake() = dev.kord.common.entity.Snowflake(this.value)
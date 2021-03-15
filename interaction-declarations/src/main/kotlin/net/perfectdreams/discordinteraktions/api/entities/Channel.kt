package net.perfectdreams.discordinteraktions.api.entities

import dev.kord.common.entity.Snowflake

interface Channel {

    val id: Snowflake

}

inline val Channel.idLong get() = id.value

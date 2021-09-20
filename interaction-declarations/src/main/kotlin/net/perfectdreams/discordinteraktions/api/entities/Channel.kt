package net.perfectdreams.discordinteraktions.api.entities

import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.Snowflake

interface Channel {
    val id: Snowflake
    val type: ChannelType
}
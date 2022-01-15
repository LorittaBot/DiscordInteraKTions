package net.perfectdreams.discordinteraktions.common.entities

import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.Snowflake

interface Channel {
    val id: Snowflake
    val type: ChannelType
}
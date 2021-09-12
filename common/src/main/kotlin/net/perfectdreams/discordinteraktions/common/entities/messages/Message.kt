package net.perfectdreams.discordinteraktions.common.entities.messages

import dev.kord.common.entity.Snowflake

interface Message {
    val id: Snowflake
    val content: String?
}
package net.perfectdreams.discordinteraktions.common.entities.messages

import net.perfectdreams.discordinteraktions.api.entities.Snowflake

interface Message {
    val id: Snowflake
    val content: String?
}
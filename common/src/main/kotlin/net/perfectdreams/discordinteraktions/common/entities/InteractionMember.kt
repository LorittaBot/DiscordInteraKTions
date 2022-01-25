package net.perfectdreams.discordinteraktions.common.entities

import dev.kord.common.entity.Permissions
import dev.kord.common.entity.Snowflake
import kotlinx.datetime.Instant

interface InteractionMember : Member {
    val permissions: Permissions
}
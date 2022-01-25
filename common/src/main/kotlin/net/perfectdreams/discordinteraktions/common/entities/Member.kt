package net.perfectdreams.discordinteraktions.common.entities

import dev.kord.common.entity.Snowflake
import kotlinx.datetime.Instant

interface Member {
    val user: User
    val nick: String?
    val roles: List<Snowflake>
    val joinedAt: Instant
    val premiumSince: Instant?
    val pending: Boolean
    val avatar: UserAvatar?
    val communicationDisabledUntil: Instant?
}
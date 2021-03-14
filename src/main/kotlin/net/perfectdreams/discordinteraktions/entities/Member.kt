package net.perfectdreams.discordinteraktions.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.LongAsStringSerializer

@Serializable
class Member(
    val user: User,
    val nick: String? = null,
    val deaf: Boolean,
    @SerialName("is_pending")
    val isPending: Boolean,
    @SerialName("joined_at")
    val joinedAt: String,
    val mute: Boolean,
    val pending: Boolean,
    @Serializable(with = LongAsStringSerializer::class)
    val permissions: Long,
    @SerialName("premium_since")
    val premiumSince: String?,
    @SerialName("roles")
    val roles: List<Long>
)
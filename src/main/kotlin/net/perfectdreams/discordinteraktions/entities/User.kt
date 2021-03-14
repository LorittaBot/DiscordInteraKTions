package net.perfectdreams.discordinteraktions.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.LongAsStringSerializer

@Serializable
class User(
    @Serializable(with = LongAsStringSerializer::class)
    val id: Long,
    val username: String,
    val discriminator: String,
    val avatar: String,
    @SerialName("public_flags")
    val publicFlags: Int,
)
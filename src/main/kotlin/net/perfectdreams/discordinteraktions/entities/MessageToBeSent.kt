package net.perfectdreams.discordinteraktions.entities

import kotlinx.serialization.Serializable

@Serializable
class MessageToBeSent(
    val content: String,
    val tts: Boolean? = null,
    val flags: Int? = null
)
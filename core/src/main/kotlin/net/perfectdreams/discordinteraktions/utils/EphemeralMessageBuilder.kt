package net.perfectdreams.discordinteraktions.utils

import dev.kord.common.entity.AllowedMentions
import dev.kord.common.entity.MessageFlags
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.io.InputStream

class EphemeralMessageBuilder {
    var content: String? = null
    var allowedMentions: AllowedMentions? = null
}
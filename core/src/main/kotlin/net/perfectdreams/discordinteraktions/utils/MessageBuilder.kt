package net.perfectdreams.discordinteraktions.utils

import dev.kord.common.entity.AllowedMentions
import dev.kord.common.entity.DiscordEmbed
import dev.kord.common.entity.MessageFlags
import dev.kord.rest.json.request.EmbedRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.io.InputStream

fun buildMessage(block: MessageBuilder.() -> (Unit)): InteractionMessage {
    val result = MessageBuilder().apply(block)

    return InteractionMessage(
        result.content!!,
        result.tts,
        result.allowedMentions,
        result.flags,
        result.files,
        result.embed?.intoSerial(),
        result.embed
    )
}

class MessageBuilder {
    var content: String? = null
    var tts: Boolean? = null
    // var embeds: List<S>
    var allowedMentions: AllowedMentions? = null
    var flags: MessageFlags? = null

    var files = mutableMapOf<String, InputStream>()
    var embed: Embed? = null

    fun addFile(fileName: String, stream: InputStream) {
        files[fileName] = stream
    }
}

@Serializable
data class InteractionMessage(
    val content: String,
    val tts: Boolean? = null,
    @SerialName("allowed_mentions")
    val allowedMentions: AllowedMentions? = null,
    val flags: MessageFlags? = null,
    @Transient
    val files: Map<String, InputStream>? = null,
    @SerialName("embed")
    val kordEmbed: DiscordEmbed? = null,
    @Transient
    val embed: Embed? = null
)
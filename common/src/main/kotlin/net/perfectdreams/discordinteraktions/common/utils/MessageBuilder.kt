package net.perfectdreams.discordinteraktions.common.utils

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.io.InputStream

fun buildMessage(block: MessageBuilder.() -> (Unit)): InteractionMessage {
    val result = MessageBuilder().apply(block)

    return InteractionMessage(
        result.content.orEmpty(),
        result.tts,
        // TODO:
        // result.allowedMentions,
        // result.flags,
        result.isEphemeral,
        result.files,
        result.components
        // result.embed,
        // listOfNotNull(result.embed?.intoSerial())
    )
}

class MessageBuilder {
    var content: String? = null
    var tts: Boolean? = null
    // var embeds: List<S>
    // TODO:
    // var allowedMentions: AllowedMentions? = null
    /**
     * If set to true, the message will be ephemeral
     * If set to false, the message won't be ephemeral
     * If not set, the message will follow what was used in the defer(...) call
     */
    var isEphemeral: Boolean? = null
    // var embed: Embed? = null
    var files = mutableMapOf<String, InputStream>()
    val components = mutableListOf<MessageComponent>()
    // TODO:
    // fun embed(declaration: Embed.() -> Unit) {
    //     this.embed = Embed().apply(declaration)
    // }

    fun addFile(fileName: String, stream: InputStream) {
        files[fileName] = stream
    }
}

data class InteractionMessage(
    val content: String,
    val tts: Boolean? = null,
    // @SerialName("allowed_mentions")
    // val allowedMentions: AllowedMentions? = null,
    var isEphemeral: Boolean?,
    // val flags: MessageFlags? = null,
    val files: Map<String, InputStream>? = null,
    val components: List<MessageComponent>
    // @Transient
    // val abstractEmbed: Embed? = null,
    // val embeds: List<DiscordEmbed> = listOf()
)
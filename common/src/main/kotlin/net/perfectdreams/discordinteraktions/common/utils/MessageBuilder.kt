package net.perfectdreams.discordinteraktions.common.utils

import net.perfectdreams.discordinteraktions.common.components.actionrow.ActionRowBuilder
import java.io.InputStream

fun buildMessage(block: MessageBuilder.() -> (Unit)): InteractionMessage {
    val result = MessageBuilder().apply(block)

    return InteractionMessage(
        result.content.orEmpty(),
        result.tts,
        result.files,
        result.embeds,
        result.allowedMentions,
        false,
        result.components
    )
}

@InteraKTionsDslMarker
class MessageBuilder {
    var content: String? = null
    var tts: Boolean? = null
    var allowedMentions: AllowedMentions? = null
    internal var embeds: MutableList<EmbedBuilder>? = null
    internal var files = mutableMapOf<String, InputStream>()
    internal var components = mutableListOf<ActionRowComponent>()

    fun embed(block: EmbedBuilder.() -> Unit) {
        embeds = (embeds ?: mutableListOf()).also {
            it.add(EmbedBuilder().apply(block))
        }
    }

    fun actionRow(block: ActionRowBuilder.() -> Unit) {
        components = (components ?: mutableListOf()).also {
            it.add(
                ActionRowBuilder()
                    .apply(block)
                    .build()
            )
        }
    }

    fun file(fileName: String, stream: InputStream) {
        files[fileName] = stream
    }
}

// Follows Discord's field order
// https://discord.com/developers/docs/resources/channel#create-message
data class InteractionMessage(
    val content: String,
    val tts: Boolean? = null,
    val files: Map<String, InputStream>? = null,
    val embeds: List<EmbedBuilder>? = null,
    val allowedMentions: AllowedMentions? = null,
    var isEphemeral: Boolean,
    val components: List<ActionRowComponent>? = null
)
package net.perfectdreams.discordinteraktions.common.utils

import net.perfectdreams.discordinteraktions.api.entities.optional.Optional
import net.perfectdreams.discordinteraktions.api.entities.optional.delegate.delegate
import net.perfectdreams.discordinteraktions.common.components.ComponentsBuilder
import java.io.InputStream

fun buildMessage(block: MessageEditBuilder.() -> (Unit)): InteractionCreateMessage {
    val result = MessageEditBuilder().apply(block)

    return InteractionCreateMessage(
        result.content,
        result.tts,
        result.removeAlreadyUploadedFiles,
        result.files,
        result.embeds,
        result.allowedMentions,
        false,
        result.components
    )
}

@InteraKTionsDslMarker
class MessageEditBuilder {
    private val state = MessageModifyStateHolder()

    var content by state::content.delegate()
    var allowedMentions by state::allowedMentions.delegate()
    internal var embeds by state::embeds.delegate()
    internal var files by state::files.delegate()
    internal var components by state::components.delegate()

    fun embed(block: EmbedBuilder.() -> Unit) {
        embeds = (embeds ?: mutableListOf()).also {
            it.add(EmbedBuilder().apply(block))
        }
    }

    fun components(block: ComponentsBuilder.() -> Unit) {
        components = ComponentsBuilder().apply(block).build()
    }

    fun file(fileName: String, stream: InputStream) {
        files[fileName] = stream
    }
}

// Follows Discord's field order
// https://discord.com/developers/docs/resources/channel#create-message
data class InteractionEditMessage(
    val content: String?,
    val tts: Boolean?,
    val removeAlreadyUploadedFiles: Boolean,
    val files: Map<String, InputStream>?,
    val embeds: List<EmbedBuilder>?,
    val allowedMentions: AllowedMentions?,
    var isEphemeral: Boolean,
    val components: List<ActionRowComponent>?
)
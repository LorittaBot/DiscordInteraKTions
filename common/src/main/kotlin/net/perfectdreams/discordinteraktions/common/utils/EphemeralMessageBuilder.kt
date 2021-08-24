package net.perfectdreams.discordinteraktions.common.utils

import net.perfectdreams.discordinteraktions.common.components.actionrow.ActionRowBuilder

fun buildEphemeralMessage(block: EphemeralMessageBuilder.() -> (Unit)): InteractionMessage {
    val result = EphemeralMessageBuilder().apply(block)

    return InteractionMessage(
        result.content.orEmpty(),
        result.tts,
        null,
        result.embeds,
        result.allowedMentions,
        true,
        result.components
    )
}

@InteraKTionsDslMarker
class EphemeralMessageBuilder {
    var content: String? = null
    var tts: Boolean? = null
    var allowedMentions: AllowedMentions? = null
    internal var embeds: MutableList<EmbedBuilder>? = null
    internal var components: MutableList<ActionRowComponent>? = null

    fun embed(declaration: EmbedBuilder.() -> Unit) {
        embeds = (embeds ?: mutableListOf()).also {
            it.add(EmbedBuilder().apply(declaration))
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
}
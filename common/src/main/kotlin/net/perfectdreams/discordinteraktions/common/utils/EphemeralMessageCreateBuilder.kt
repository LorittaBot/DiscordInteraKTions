package net.perfectdreams.discordinteraktions.common.utils

import net.perfectdreams.discordinteraktions.common.components.actionrow.ActionRowBuilder

fun buildEphemeralMessage(block: EphemeralMessageCreateBuilder.() -> (Unit)): InteractionCreateMessage {
    val result = EphemeralMessageCreateBuilder().apply(block)

    return InteractionCreateMessage(
        result.content.orEmpty(),
        result.tts,
        false,
        null,
        result.embeds,
        result.allowedMentions,
        true,
        result.components
    )
}

@InteraKTionsDslMarker
class EphemeralMessageCreateBuilder {
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
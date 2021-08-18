package net.perfectdreams.discordinteraktions.common.utils

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
    var embeds: MutableList<EmbedBuilder>? = null
    val components = mutableListOf<MessageComponent>()

    fun embed(declaration: EmbedBuilder.() -> Unit) {
        embeds = (embeds ?: mutableListOf()).also {
            it.add(EmbedBuilder().apply(declaration))
        }
    }
}
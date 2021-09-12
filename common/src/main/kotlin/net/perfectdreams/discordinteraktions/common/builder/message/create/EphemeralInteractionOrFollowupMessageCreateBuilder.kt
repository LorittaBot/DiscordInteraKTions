package net.perfectdreams.discordinteraktions.common.builder.message.create

import dev.kord.rest.builder.component.MessageComponentBuilder
import dev.kord.rest.builder.message.AllowedMentionsBuilder
import dev.kord.rest.builder.message.EmbedBuilder

// From Kord, however this is a interaction OR followup create builder
class EphemeralInteractionOrFollowupMessageCreateBuilder : EphemeralMessageCreateBuilder {
    override var content: String? = null

    override var tts: Boolean? = null

    override var embeds: MutableList<EmbedBuilder>? = mutableListOf()

    override var allowedMentions: AllowedMentionsBuilder? = null

    override var components: MutableList<MessageComponentBuilder>? = mutableListOf()
}
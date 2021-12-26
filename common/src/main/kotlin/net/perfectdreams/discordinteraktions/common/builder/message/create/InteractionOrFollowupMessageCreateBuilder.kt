package net.perfectdreams.discordinteraktions.common.builder.message.create

import dev.kord.rest.NamedFile
import dev.kord.rest.builder.component.MessageComponentBuilder
import dev.kord.rest.builder.message.AllowedMentionsBuilder
import dev.kord.rest.builder.message.EmbedBuilder

/**
 * Message builder for publicly responding to an interaction.
 */
// From Kord, however this is a interaction OR followup create builder
class InteractionOrFollowupMessageCreateBuilder(val ephemeral: Boolean) : MessageCreateBuilder {
    override var content: String? = null

    override var tts: Boolean? = null

    override var embeds: MutableList<EmbedBuilder>? = mutableListOf()

    override var allowedMentions: AllowedMentionsBuilder? = null

    override var components: MutableList<MessageComponentBuilder>? = mutableListOf()

    override var files: MutableList<NamedFile>? = mutableListOf()
}
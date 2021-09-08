package net.perfectdreams.discordinteraktions.common.builder.message.modify

import dev.kord.common.entity.optional.delegate.delegate
import dev.kord.rest.builder.component.MessageComponentBuilder
import dev.kord.rest.builder.message.AllowedMentionsBuilder
import dev.kord.rest.builder.message.EmbedBuilder

// From Kord, however this is a interaction OR followup modify builder
class EphemeralInteractionOrFollowupMessageModifyBuilder : EphemeralMessageModifyBuilder {
    // We need to access the delegated stuff ourselves
    var state = MessageModifyStateHolder()

    override var content: String? by state::content.delegate()

    override var embeds: MutableList<EmbedBuilder>? by state::embeds.delegate()

    override var allowedMentions: AllowedMentionsBuilder? by state::allowedMentions.delegate()

    override var components: MutableList<MessageComponentBuilder>? by state::components.delegate()
}
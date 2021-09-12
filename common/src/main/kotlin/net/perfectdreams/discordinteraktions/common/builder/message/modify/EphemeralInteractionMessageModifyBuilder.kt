package net.perfectdreams.discordinteraktions.common.builder.message.modify

import dev.kord.common.entity.optional.delegate.delegate
import dev.kord.rest.builder.component.MessageComponentBuilder
import dev.kord.rest.builder.message.AllowedMentionsBuilder
import dev.kord.rest.builder.message.EmbedBuilder

// From Kord, we copied this to allow our extension methods to be available when using methods that use modify
class EphemeralInteractionMessageModifyBuilder : EphemeralMessageModifyBuilder {
    // We need to access the delegated stuff ourselves
    override var state = MessageModifyStateHolder()

    override var content: String? by state::content.delegate()

    override var embeds: MutableList<EmbedBuilder>? by state::embeds.delegate()

    override var allowedMentions: AllowedMentionsBuilder? by state::allowedMentions.delegate()

    override var components: MutableList<MessageComponentBuilder>? by state::components.delegate()
}
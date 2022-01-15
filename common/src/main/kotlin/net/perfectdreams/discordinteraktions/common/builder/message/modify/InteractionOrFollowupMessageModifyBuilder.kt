package net.perfectdreams.discordinteraktions.common.builder.message.modify

import dev.kord.common.entity.DiscordAttachment
import dev.kord.common.entity.optional.delegate.delegate
import dev.kord.rest.NamedFile
import dev.kord.rest.builder.component.MessageComponentBuilder
import dev.kord.rest.builder.message.AllowedMentionsBuilder
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.modify.FollowupMessageModifyBuilder
import dev.kord.rest.builder.message.modify.InteractionResponseModifyBuilder
import net.perfectdreams.discordinteraktions.platforms.kord.utils.runIfNotMissing

// From Kord, however this is a interaction OR followup modify builder
class InteractionOrFollowupMessageModifyBuilder : MessageModifyBuilder {
    // We need to access the delegated stuff ourselves
    var state = MessageModifyStateHolder()

    override var files: MutableList<NamedFile>? by state::files.delegate()

    override var attachments: MutableList<DiscordAttachment>? by state::attachments.delegate()

    override var content: String? by state::content.delegate()

    override var embeds: MutableList<EmbedBuilder>? by state::embeds.delegate()

    override var allowedMentions: AllowedMentionsBuilder? by state::allowedMentions.delegate()

    override var components: MutableList<MessageComponentBuilder>? by state::components.delegate()

    override fun toFollowupMessageModifyBuilder(): FollowupMessageModifyBuilder {
        return FollowupMessageModifyBuilder().apply {
            runIfNotMissing(state.content) { this.content = it }
            runIfNotMissing(state.allowedMentions) { this.allowedMentions = it }
            runIfNotMissing(state.components) { this.components = it }
            runIfNotMissing(state.embeds) { this.embeds = it }
            runIfNotMissing(state.attachments) { this.attachments = it }
            runIfNotMissing(state.files) { this.files = it }
        }
    }

    override fun toInteractionMessageResponseModifyBuilder(): InteractionResponseModifyBuilder {
        return InteractionResponseModifyBuilder().apply {
            runIfNotMissing(state.content) { this.content = it }
            runIfNotMissing(state.allowedMentions) { this.allowedMentions = it }
            runIfNotMissing(state.components) { this.components = it }
            runIfNotMissing(state.embeds) { this.embeds = it }
            runIfNotMissing(state.attachments) { this.attachments = it }
            runIfNotMissing(state.files) { this.files = it }
        }
    }
}
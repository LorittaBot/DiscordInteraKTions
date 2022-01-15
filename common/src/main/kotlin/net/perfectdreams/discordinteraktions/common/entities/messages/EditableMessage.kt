package net.perfectdreams.discordinteraktions.common.entities.messages

import dev.kord.rest.builder.message.modify.FollowupMessageModifyBuilder
import dev.kord.rest.builder.message.modify.InteractionResponseModifyBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder
import net.perfectdreams.discordinteraktions.platforms.kord.utils.runIfNotMissing

interface EditableMessage {
    suspend fun editMessage(block: InteractionOrFollowupMessageModifyBuilder.() -> (Unit)): EditableMessage
    suspend fun editMessage(message: InteractionOrFollowupMessageModifyBuilder): EditableMessage

    fun convertToInteractionResponseModifyBuilder(message: InteractionOrFollowupMessageModifyBuilder) = InteractionResponseModifyBuilder().apply {
        runIfNotMissing(message.state.content) { this.content = it }
        runIfNotMissing(message.state.allowedMentions) { this.allowedMentions = it }
        runIfNotMissing(message.state.components) { this.components = it }
        runIfNotMissing(message.state.embeds) { this.embeds = it }
        runIfNotMissing(message.state.attachments) { this.attachments = it }
        runIfNotMissing(message.state.files) { this.files = it }
    }

    fun convertToFollowupMessageModifyBuilder(message: InteractionOrFollowupMessageModifyBuilder) = FollowupMessageModifyBuilder().apply {
        runIfNotMissing(message.state.content) { this.content = it }
        runIfNotMissing(message.state.allowedMentions) { this.allowedMentions = it }
        runIfNotMissing(message.state.components) { this.components = it }
        runIfNotMissing(message.state.embeds) { this.embeds = it }
        runIfNotMissing(message.state.attachments) { this.attachments = it }
        runIfNotMissing(message.state.files) { this.files = it }
    }
}
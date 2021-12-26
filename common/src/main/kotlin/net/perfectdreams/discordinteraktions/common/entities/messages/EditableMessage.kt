package net.perfectdreams.discordinteraktions.common.entities.messages

import dev.kord.common.entity.optional.Optional
import dev.kord.rest.builder.message.modify.FollowupMessageModifyBuilder
import dev.kord.rest.builder.message.modify.InteractionResponseModifyBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder

interface EditableMessage {
    suspend fun editMessage(block: InteractionOrFollowupMessageModifyBuilder.() -> (Unit)): EditableMessage
    suspend fun editMessage(message: InteractionOrFollowupMessageModifyBuilder): EditableMessage

    fun convertToInteractionResponseModifyBuilder(message: InteractionOrFollowupMessageModifyBuilder) = InteractionResponseModifyBuilder().apply {
        runIfNotMissing(message.state.content) { this.content = it }
        runIfNotMissing(message.state.allowedMentions) { this.allowedMentions = it }
        runIfNotMissing(message.state.components) { this.components = it }
        runIfNotMissing(message.state.embeds) { this.embeds = it }
        runIfNotMissing(message.state.attachments) { this.attachments = it }
        runIfNotMissing(message.state.files) { this.files = files }
    }

    fun convertToFollowupMessageModifyBuilder(message: InteractionOrFollowupMessageModifyBuilder) = FollowupMessageModifyBuilder().apply {
        runIfNotMissing(message.state.content) { this.content = it }
        runIfNotMissing(message.state.allowedMentions) { this.allowedMentions = it }
        runIfNotMissing(message.state.components) { this.components = it }
        runIfNotMissing(message.state.embeds) { this.embeds = it }
        runIfNotMissing(message.state.attachments) { this.attachments = it }
        runIfNotMissing(message.state.files) { this.files = files }
    }

    fun <T> runIfNotMissing(optional: Optional<T>, callback: (T?) -> (Unit)) {
        if (optional !is Optional.Missing)
            callback.invoke(optional.value)
    }
}
package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.message.modify.InteractionResponseModifyBuilder
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.builder.message.modify.PersistentMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.modify.PublicInteractionOrFollowupMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.entities.messages.EditablePersistentMessage
import net.perfectdreams.discordinteraktions.common.entities.messages.PublicMessage
import net.perfectdreams.discordinteraktions.platforms.kord.utils.runIfNotMissing

class KordOriginalInteractionPublicMessage(
    private val rest: RestClient,
    private val applicationId: Snowflake,
    private val interactionToken: String,
    override val content: String?
) : PublicMessage, EditablePersistentMessage {
    override val id: Snowflake
        get() = error("Original Interaction Messages do not have an ID!")

    override suspend fun editMessage(block: PersistentMessageModifyBuilder.() -> Unit): EditablePersistentMessage = editMessage(PublicInteractionOrFollowupMessageModifyBuilder().apply(block))

    override suspend fun editMessage(message: PersistentMessageModifyBuilder): EditablePersistentMessage {
        val newMessage = rest.interaction.modifyInteractionResponse(
            applicationId,
            interactionToken,
            InteractionResponseModifyBuilder().apply {
                runIfNotMissing(message.state.content) { this.content = it }
                runIfNotMissing(message.state.allowedMentions) { this.allowedMentions = it }
                runIfNotMissing(message.state.components) { this.components = it }
                runIfNotMissing(message.state.embeds) { this.embeds = it }
                runIfNotMissing(message.state.files) { this.files = it }
                runIfNotMissing(message.state.attachments) { this.attachments = it }
            }.toRequest()
        )

        return KordEditedOriginalInteractionPublicMessage(
            rest,
            applicationId,
            interactionToken,
            newMessage
        )
    }
}
package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.message.modify.FollowupMessageModifyBuilder
import dev.kord.rest.service.RestClient
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.perfectdreams.discordinteraktions.common.builder.message.modify.PersistentMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.modify.PublicInteractionOrFollowupMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.entities.messages.EditablePersistentMessage
import net.perfectdreams.discordinteraktions.platforms.kord.utils.runIfNotMissing

@Serializable
class KordEditedOriginalInteractionPublicMessage(
    @Contextual
    private val rest: RestClient,
    private val applicationId: Snowflake,
    private val interactionToken: String,
    private val message: DiscordMessage
) : KordPublicMessage(message), EditablePersistentMessage {
    override suspend fun editMessage(block: PersistentMessageModifyBuilder.() -> Unit): EditablePersistentMessage = editMessage(PublicInteractionOrFollowupMessageModifyBuilder().apply(block))

    override suspend fun editMessage(message: PersistentMessageModifyBuilder): EditablePersistentMessage {
        val newMessage = rest.interaction.modifyFollowupMessage(
            applicationId,
            interactionToken,
            this.message.id,
            FollowupMessageModifyBuilder().apply {
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
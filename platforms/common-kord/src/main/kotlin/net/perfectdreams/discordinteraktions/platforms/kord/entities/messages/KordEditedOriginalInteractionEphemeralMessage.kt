package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.message.modify.EphemeralInteractionResponseModifyBuilder
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.builder.message.modify.EphemeralInteractionOrFollowupMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.modify.EphemeralMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.entities.messages.EditableEphemeralMessage
import net.perfectdreams.discordinteraktions.platforms.kord.utils.runIfNotMissing

class KordEditedOriginalInteractionEphemeralMessage(
    private val rest: RestClient,
    private val applicationId: Snowflake,
    private val interactionToken: String,
    private val message: DiscordMessage
) : KordEphemeralMessage(message), EditableEphemeralMessage {
    override suspend fun editMessage(block: EphemeralMessageModifyBuilder.() -> Unit): EditableEphemeralMessage = editMessage(EphemeralInteractionOrFollowupMessageModifyBuilder().apply(block))

    override suspend fun editMessage(message: EphemeralMessageModifyBuilder): EditableEphemeralMessage {
        val newMessage = rest.interaction.modifyInteractionResponse(
            applicationId,
            interactionToken,
            EphemeralInteractionResponseModifyBuilder().apply {
                runIfNotMissing(message.state.content) { this.content = it }
                runIfNotMissing(message.state.allowedMentions) { this.allowedMentions = it }
                runIfNotMissing(message.state.components) { this.components = it }
                runIfNotMissing(message.state.embeds) { this.embeds = it }
            }.toRequest()
        )

        return KordEditedOriginalInteractionEphemeralMessage(
            rest,
            applicationId,
            interactionToken,
            newMessage
        )
    }
}
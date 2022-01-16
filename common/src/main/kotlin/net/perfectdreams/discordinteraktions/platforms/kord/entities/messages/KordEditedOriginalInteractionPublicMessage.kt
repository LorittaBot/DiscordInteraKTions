package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.entities.messages.EditableMessage
import net.perfectdreams.discordinteraktions.common.utils.suspendableApply

class KordEditedOriginalInteractionPublicMessage(
    private val rest: RestClient,
    private val applicationId: Snowflake,
    private val interactionToken: String,
    data: DiscordMessage
) : KordPublicMessage(data), EditableMessage {
    override suspend fun editMessage(block: suspend InteractionOrFollowupMessageModifyBuilder.() -> Unit): EditableMessage = editMessage(InteractionOrFollowupMessageModifyBuilder().suspendableApply(block))

    override suspend fun editMessage(message: InteractionOrFollowupMessageModifyBuilder): EditableMessage {
        val newMessage = rest.interaction.modifyFollowupMessage(
            applicationId,
            interactionToken,
            this.data.id,
            message.toFollowupMessageModifyBuilder().toRequest()
        )

        return KordEditedOriginalInteractionPublicMessage(
            rest,
            applicationId,
            interactionToken,
            newMessage
        )
    }
}
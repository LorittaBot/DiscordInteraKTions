package net.perfectdreams.discordinteraktions.common.modals

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.common.builder.message.create.InteractionOrFollowupMessageCreateBuilder
import net.perfectdreams.discordinteraktions.common.entities.User
import net.perfectdreams.discordinteraktions.common.entities.messages.EditableMessage
import net.perfectdreams.discordinteraktions.common.entities.messages.Message
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData
import net.perfectdreams.discordinteraktions.common.requests.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.requests.RequestBridge

open class ModalSubmitContext(
    var bridge: RequestBridge,
    val sender: User,
    val channelId: Snowflake,
    val data: InteractionData,
    val discordInteractionData: DiscordInteraction
) {
    val isDeferred
        get() = bridge.state.value != InteractionRequestState.NOT_REPLIED_YET
    var wasInitiallyDeferredEphemerally = false

    suspend inline fun sendMessage(block: InteractionOrFollowupMessageCreateBuilder.() -> (Unit))
            = sendPublicMessage(InteractionOrFollowupMessageCreateBuilder(false).apply(block))

    suspend inline fun sendEphemeralMessage(block: InteractionOrFollowupMessageCreateBuilder.() -> (Unit))
            = sendEphemeralMessage(InteractionOrFollowupMessageCreateBuilder(true).apply(block))

    suspend fun sendPublicMessage(message: InteractionOrFollowupMessageCreateBuilder): EditableMessage {
        // Check if state matches what we expect
        if (bridge.state.value == InteractionRequestState.DEFERRED_CHANNEL_MESSAGE)
            if (wasInitiallyDeferredEphemerally)
                error("Trying to send a public message but the message was originally deferred ephemerally! Change the \"deferMessage(...)\" call to be public")

        if (message.files?.isNotEmpty() == true && !isDeferred) {
            // If the message has files and our current bridge state is "NOT_REPLIED_YET", then it means that we need to defer before sending the file!
            // (Because currently you can only send files by editing the original interaction message or with a follow up message
            error("Deferring modal submits is not supported!")
        }

        return bridge.manager.sendPublicMessage(message)
    }

    suspend fun sendEphemeralMessage(message: InteractionOrFollowupMessageCreateBuilder): EditableMessage {
        // Check if state matches what we expect
        if (bridge.state.value == InteractionRequestState.DEFERRED_CHANNEL_MESSAGE)
            if (!wasInitiallyDeferredEphemerally)
                error("Trying to send a ephemeral message but the message was originally deferred as public! Change the \"deferMessage(...)\" call to be ephemeral")

        if (message.files?.isNotEmpty() == true && !isDeferred) {
            // If the message has files and our current bridge state is "NOT_REPLIED_YET", then it means that we need to defer before sending the file!
            // (Because currently you can only send files by editing the original interaction message or with a follow up message
            error("Deferring modal submits is not supported!")
        }

        return bridge.manager.sendEphemeralMessage(message)
    }
}
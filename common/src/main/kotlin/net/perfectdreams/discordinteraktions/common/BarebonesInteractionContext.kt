package net.perfectdreams.discordinteraktions.common

import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.component.ActionRowBuilder
import dev.kord.rest.builder.interaction.ModalBuilder
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.builder.message.create.InteractionOrFollowupMessageCreateBuilder
import net.perfectdreams.discordinteraktions.common.entities.messages.EditableMessage
import net.perfectdreams.discordinteraktions.common.modals.ModalSubmitExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.requests.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.requests.RequestBridge
import net.perfectdreams.discordinteraktions.common.requests.managers.HttpRequestManager
import net.perfectdreams.discordinteraktions.common.utils.Observable

/**
 * This is a "barebones" implementation of a [InteractionContext], where only the essential constructor parameters are present.
 *
 * This is useful if you are trying to reply to an interaction where you only have its essential information (like the interaction token).
 */
open class BarebonesInteractionContext(
    var bridge: RequestBridge
) {
    val isDeferred
        get() = bridge.state.value != InteractionRequestState.NOT_REPLIED_YET
    var wasInitiallyDeferredEphemerally = false

    /**
     * Defers the application command request message with a public message
     */
    suspend fun deferChannelMessage() {
        if (isDeferred)
            error("Trying to defer something that was already deferred!")

        bridge.manager.deferChannelMessage()
        wasInitiallyDeferredEphemerally = false
    }

    /**
     * Defers the application command request message with a ephemeral message
     */
    suspend fun deferChannelMessageEphemerally() {
        if (isDeferred)
            error("Trying to defer something that was already deferred!")

        bridge.manager.deferChannelMessageEphemerally()
        wasInitiallyDeferredEphemerally = true
    }

    suspend inline fun sendMessage(block: InteractionOrFollowupMessageCreateBuilder.() -> (Unit)) =
        sendPublicMessage(InteractionOrFollowupMessageCreateBuilder(false).apply(block))

    suspend inline fun sendEphemeralMessage(block: InteractionOrFollowupMessageCreateBuilder.() -> (Unit)) =
        sendEphemeralMessage(InteractionOrFollowupMessageCreateBuilder(true).apply(block))

    suspend fun sendPublicMessage(message: InteractionOrFollowupMessageCreateBuilder): EditableMessage {
        // Check if state matches what we expect
        if (bridge.state.value == InteractionRequestState.DEFERRED_CHANNEL_MESSAGE)
            if (wasInitiallyDeferredEphemerally)
                error("Trying to send a public message but the message was originally deferred ephemerally! Change the \"deferMessage(...)\" call to be public")

        if (message.files?.isNotEmpty() == true && !isDeferred) {
            // If the message has files and our current bridge state is "NOT_REPLIED_YET", then it means that we need to defer before sending the file!
            // (Because currently you can only send files by editing the original interaction message or with a follow up message
            deferChannelMessage()
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
            deferChannelMessage()
        }

        return bridge.manager.sendEphemeralMessage(message)
    }

    suspend fun sendModal(declaration: ModalSubmitExecutorDeclaration, title: String, data: String? = null) {
        val rows = mutableListOf(
            ActionRowBuilder(),
            ActionRowBuilder(),
            ActionRowBuilder(),
            ActionRowBuilder(),
            ActionRowBuilder()
        )

        return bridge.manager.sendModal(title, "${declaration.id}:$data") {
            declaration.options.arguments.forEach {
                if (it.actionRowNumber !in 0..4) error("")

                rows[it.actionRowNumber].apply {
                    textInput(it.style, it.customId, it.label) {
                        allowedLength = it.allowedLength
                        placeholder = it.placeholder
                        value = it.value
                        required = it.required
                    }
                }

                components.add(rows[it.actionRowNumber])
            }
        }
    }
}

/**
 * Creates a [BarebonesInteractionContext] with the [rest], [applicationId], [interactionToken] and [requestState].
 *
 * This is useful if you are trying to reply to an interaction where you only have its essential information (like the interaction token).
 */
fun BarebonesInteractionContext(
    rest: RestClient,
    applicationId: Snowflake,
    interactionToken: String,
    requestState: InteractionRequestState = InteractionRequestState.ALREADY_REPLIED
): BarebonesInteractionContext {
    val bridge = RequestBridge(Observable(requestState))

    bridge.manager = HttpRequestManager(
        bridge,
        rest,
        applicationId,
        interactionToken
    )

    return BarebonesInteractionContext(bridge)
}
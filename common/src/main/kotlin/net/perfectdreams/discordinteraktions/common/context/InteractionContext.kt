package net.perfectdreams.discordinteraktions.common.context

import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.entities.messages.EphemeralMessage
import net.perfectdreams.discordinteraktions.common.entities.messages.Message
import net.perfectdreams.discordinteraktions.common.entities.messages.PublicMessage
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData
import net.perfectdreams.discordinteraktions.common.utils.EphemeralMessageBuilder
import net.perfectdreams.discordinteraktions.common.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.common.utils.MessageBuilder
import net.perfectdreams.discordinteraktions.common.utils.buildEphemeralMessage
import net.perfectdreams.discordinteraktions.common.utils.buildMessage

abstract class InteractionContext(
    var bridge: RequestBridge,
    val sender: User,
    val data: InteractionData
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

    suspend fun sendEphemeralMessage(block: EphemeralMessageBuilder.() -> (Unit)): EphemeralMessage {
        val message = buildEphemeralMessage(block)
        // TODO: Improve the code below to always be sure that it will return an ephemeral message
        return sendMessage(message) as EphemeralMessage // This will always return an ephemeral message
    }

    suspend fun sendMessage(block: MessageBuilder.() -> (Unit)): PublicMessage {
        val message = buildMessage(block)
        // TODO: Improve the code below to always be sure that it will return an public message
        return sendMessage(message) as PublicMessage // This will always return an public message
    }

    private suspend fun sendMessage(message: InteractionMessage): Message {
        if (message.isEphemeral && message.files?.isNotEmpty() == true)
            error("Ephemeral messages cannot contain attachments!")

        // Check if state matches what we expect
        if (bridge.state.value == InteractionRequestState.DEFERRED_CHANNEL_MESSAGE)
            if (wasInitiallyDeferredEphemerally != message.isEphemeral)
                if (message.isEphemeral)
                    error("Trying to send a ephemeral message but the message was originally deferred as public! Change the \"deferMessage(...)\" call to be ephemeral")
                else
                    error("Trying to send a public message but the message was originally deferred as ephemeral! Change the \"deferMessage(...)\" call to be public")

        if (message.files?.isNotEmpty() == true && !isDeferred) {
            // If the message has files and our current bridge state is "NOT_REPLIED_YET", then it means that we need to defer before sending the file!
            // (Because currently you can only send files by editing the original interaction message or with a follow up message
            deferChannelMessage()
        }

        return bridge.manager.sendMessage(message)
    }
}
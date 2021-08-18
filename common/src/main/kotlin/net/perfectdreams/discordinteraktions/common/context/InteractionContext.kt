package net.perfectdreams.discordinteraktions.common.context

import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.entities.Message
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData
import net.perfectdreams.discordinteraktions.common.utils.EphemeralMessageBuilder
import net.perfectdreams.discordinteraktions.common.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.common.utils.MessageBuilder
import net.perfectdreams.discordinteraktions.common.utils.buildEphemeralMessage
import net.perfectdreams.discordinteraktions.common.utils.buildMessage

open class InteractionContext(
    internal var bridge: RequestBridge,
    val sender: User,
    val data: InteractionData
) {
    val isDeferred
        get() = bridge.state.value != InteractionRequestState.NOT_REPLIED_YET

    var wasInitiallyDeferredEphemerally = false

    /**
     * Defers the application command request
     *
     * @param isEphemeral if the deferred message should be ephemeral or not
     */
    suspend fun deferReply(isEphemeral: Boolean = false) {
        if (!isDeferred) {
            bridge.manager.deferReply(isEphemeral)
            wasInitiallyDeferredEphemerally = isEphemeral
        }
    }

    suspend fun sendEphemeralMessage(block: EphemeralMessageBuilder.() -> (Unit)): Message {
        val message = buildEphemeralMessage(block)
        return sendMessage(message)
    }

    suspend fun sendMessage(block: MessageBuilder.() -> (Unit)): Message {
        val message = buildMessage(block)
        return sendMessage(message)
    }

    suspend fun sendMessage(message: InteractionMessage): Message {
        val theRealMessageThatWillBeSent = message

        if (message.isEphemeral && message.files?.isNotEmpty() == true)
            error("Ephemeral messages cannot contain attachments!")

        if (message.files?.isNotEmpty() == true && !isDeferred) {
            // If the message has files and our current bridge state is "NOT_REPLIED_YET", then it means that we need to defer before sending the file!
            // (Because currently you can only send files by editing the original interaction message or with a follow up message
            deferReply(false)
        }

        return bridge.manager.sendMessage(theRealMessageThatWillBeSent)
    }
}
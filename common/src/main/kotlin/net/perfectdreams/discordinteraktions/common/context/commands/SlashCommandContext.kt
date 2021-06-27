package net.perfectdreams.discordinteraktions.common.context.commands

import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.entities.Message
import net.perfectdreams.discordinteraktions.common.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.common.utils.MessageBuilder
import net.perfectdreams.discordinteraktions.common.utils.buildMessage

open class SlashCommandContext(
    internal var bridge: RequestBridge,
    val sender: User
) {
    val isDeferred
        get() = bridge.state.value != InteractionRequestState.NOT_REPLIED_YET

    private var wasInitiallyDeferredEphemerally = false

    /**
     * Defers the slash command request
     *
     * @param isEphemeral if the deferred message should be ephemeral or not
     */
    suspend fun defer(isEphemeral: Boolean = false) {
        if (!isDeferred) {
            bridge.manager.defer(isEphemeral)
            wasInitiallyDeferredEphemerally = isEphemeral
        }
    }

    suspend fun sendMessage(block: MessageBuilder.() -> (Unit)): Message {
        val message = buildMessage(block)
        return sendMessage(message)
    }

    suspend fun sendMessage(message: InteractionMessage): Message {
        if (message.files?.isNotEmpty() == true && !isDeferred) {
            // If the message has files and our current bridge state is "NOT_REPLIED_YET", then it means that we need to defer before sending the file!
            // (Because currently you can only send files by editing the original interaction message or with a follow up message
            defer(false)
        }

        var theRealMessageThatWillBeSent = message
        if (message.isEphemeral == null) {
            theRealMessageThatWillBeSent = theRealMessageThatWillBeSent.copy(
                isEphemeral = wasInitiallyDeferredEphemerally
            )
        }

        if (theRealMessageThatWillBeSent.isEphemeral == true && message.files?.isNotEmpty() == true)
            throw UnsupportedOperationException("Ephemeral messages cannot contain attachments!")

        return bridge.manager.sendMessage(theRealMessageThatWillBeSent)
    }
}
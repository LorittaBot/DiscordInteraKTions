package net.perfectdreams.discordinteraktions.common.context.buttons

import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.context.InteractionContext
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.entities.DummyMessage
import net.perfectdreams.discordinteraktions.common.entities.Message
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData
import net.perfectdreams.discordinteraktions.common.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.common.utils.MessageBuilder
import net.perfectdreams.discordinteraktions.common.utils.buildMessage

class ButtonClickContext(
    bridge: RequestBridge,
    sender: User,
    data: InteractionData
) : InteractionContext(bridge, sender, data) {
    /**
     * Defers the button request
     */
    suspend fun deferEdit() {
        if (!isDeferred) {
            bridge.manager.deferEdit(null)
        }
    }

    suspend fun deferEdit(block: MessageBuilder.() -> (Unit)): Message {
        val message = buildMessage(block)
        return deferEdit(message)
    }

    suspend fun deferEdit(message: InteractionMessage): Message {
        if (message.files?.isNotEmpty() == true && !isDeferred) {
            // If the message has files and our current bridge state is "NOT_REPLIED_YET", then it means that we need to defer before sending the file!
            // (Because currently you can only send files by editing the original interaction message or with a follow up message
            deferReply(false)
        }

        var theRealMessageThatWillBeSent = message
        if (message.isEphemeral == null) {
            theRealMessageThatWillBeSent = theRealMessageThatWillBeSent.copy(
                isEphemeral = wasInitiallyDeferredEphemerally
            )
        }

        if (theRealMessageThatWillBeSent.isEphemeral == true && message.files?.isNotEmpty() == true)
            throw UnsupportedOperationException("Ephemeral messages cannot contain attachments!")

        bridge.manager.deferEdit(theRealMessageThatWillBeSent)


        return DummyMessage()
    }
}
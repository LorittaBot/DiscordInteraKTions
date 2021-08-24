package net.perfectdreams.discordinteraktions.common.context.buttons

import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.context.InteractionContext
import net.perfectdreams.discordinteraktions.common.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.entities.EphemeralMessage
import net.perfectdreams.discordinteraktions.common.entities.Message
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData
import net.perfectdreams.discordinteraktions.common.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.common.utils.MessageBuilder
import net.perfectdreams.discordinteraktions.common.utils.buildMessage

class ButtonClickContext(
    bridge: RequestBridge,
    sender: User,
    val message: Message,
    data: InteractionData
) : InteractionContext(bridge, sender, data) {
    suspend fun deferEditMessage() {
        if (!isDeferred) {
            bridge.manager.deferEditMessage()
        }
    }

    suspend fun editMessage(block: MessageBuilder.() -> (Unit)): Message {
        val message = buildMessage(block)
        return editMessage(message)
    }

    private suspend fun editMessage(message: InteractionMessage): Message {
        if (message.isEphemeral && message.files?.isNotEmpty() == true)
            error("Ephemeral messages cannot contain attachments!")

        // Check if state matches what we expect
        if (bridge.state.value == InteractionRequestState.DEFERRED_UPDATE_MESSAGE)
            if (wasInitiallyDeferredEphemerally != message.isEphemeral)
                if (message.isEphemeral)
                    error("Trying to send a ephemeral message but the message was originally deferred was public! Change the \"deferMessage(...)\" call to be ephemeral")
                else
                    error("Trying to send a public message but the message was originally deferred was ephemeral! Change the \"deferMessage(...)\" call to be public")

        if (message.files?.isNotEmpty() == true && !isDeferred) {
            // If the message has files and our current bridge state is "NOT_REPLIED_YET", then it means that we need to defer before sending the file!
            // (Because currently you can only send files by editing the original interaction message or with a follow up message
            deferMessage(false)
        }

        return bridge.manager.editMessage(message, this.message is EphemeralMessage)
    }
}
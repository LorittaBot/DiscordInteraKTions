package net.perfectdreams.discordinteraktions.common.context

import net.perfectdreams.discordinteraktions.common.entities.Message
import net.perfectdreams.discordinteraktions.common.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.common.utils.MessageBuilder
import net.perfectdreams.discordinteraktions.common.utils.buildMessage

open class SlashCommandContext(
    // TODO:
    // val request: CommandInteraction,
    // val relativeOptions: List<Option>?,
    internal var bridge: RequestBridge
) {
    val isDeferred
        get() = bridge.state.value != InteractionRequestState.NOT_REPLIED_YET

    // val user: User = KordUser(request.member.value?.user?.value ?: request.user.value ?: throw IllegalArgumentException("There isn't a user object present! Discord Bug?"))
    private var wasInitiallyDeferredEphemerally = false

    suspend fun defer(isEphemeral: Boolean) {
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
            // TODO: Check if the ephemeral flag is set to true, you can't send files in a ephemeral message!
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

    // TODO:
    /* suspend fun sendEphemeralMessage(block: EphemeralMessageBuilder.() -> (Unit)) = sendMessage {
        val ephemeralMessage = EphemeralMessageBuilder().apply(block)

        content = ephemeralMessage.content
        allowedMentions = ephemeralMessage.allowedMentions
        flags = MessageFlags(MessageFlag.Ephemeral)
    } */
}
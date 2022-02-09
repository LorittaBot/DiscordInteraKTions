package net.perfectdreams.discordinteraktions.common.requests.managers

import dev.kord.common.entity.Choice
import dev.kord.rest.builder.interaction.ModalBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.create.InteractionOrFollowupMessageCreateBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.entities.messages.EditableMessage
import net.perfectdreams.discordinteraktions.common.requests.RequestBridge

abstract class RequestManager(val bridge: RequestBridge) {
    /**
     * A deferred response is the one that you can use to
     * be able to edit the original message for 15 minutes since it was sent.
     *
     * The user will just see a loading status for the interaction.
     */
    abstract suspend fun deferChannelMessage()

    /**
     * A deferred response is the one that you can use to
     * be able to edit the original message for 15 minutes since it was sent.
     *
     * The user will just see a loading status for the interaction.
     */
    abstract suspend fun deferChannelMessageEphemerally()

    /**
     * The usual way of sending messages to a specific channel/user.
     */
    abstract suspend fun sendPublicMessage(message: InteractionOrFollowupMessageCreateBuilder): EditableMessage

    /**
     * The usual way of sending messages to a specific channel/user.
     */
    abstract suspend fun sendEphemeralMessage(message: InteractionOrFollowupMessageCreateBuilder): EditableMessage

    /**
     * A deferred response is the one that you can use to
     * be able to edit the original message for 15 minutes since it was sent.
     *
     * The user will not see a loading status for the interaction.
     */
    abstract suspend fun deferUpdateMessage()

    /**
     * The usual way of editing a message to a specific channel/user.
     */
    abstract suspend fun updateMessage(message: InteractionOrFollowupMessageModifyBuilder): EditableMessage

    abstract suspend fun sendStringAutocomplete(list: List<Choice<String>>)

    abstract suspend fun sendIntegerAutocomplete(list: List<Choice<Long>>)

    abstract suspend fun sendNumberAutocomplete(list: List<Choice<Double>>)

    abstract suspend fun sendForm(title: String, customId: String, builder: ModalBuilder.() -> Unit)
}
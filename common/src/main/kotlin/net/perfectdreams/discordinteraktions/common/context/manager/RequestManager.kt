package net.perfectdreams.discordinteraktions.common.context.manager

import net.perfectdreams.discordinteraktions.common.builder.message.create.InteractionOrFollowupMessageCreateBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.modify.InteractionOrFollowupMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.entities.messages.EditableMessage

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
}
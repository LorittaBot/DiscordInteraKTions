package net.perfectdreams.discordinteraktions.common.context.manager

import dev.kord.rest.builder.message.modify.EphemeralInteractionResponseModifyBuilder
import dev.kord.rest.builder.message.modify.PublicInteractionResponseModifyBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.create.EphemeralInteractionOrFollowupMessageCreateBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.create.PublicInteractionOrFollowupMessageCreateBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.modify.EphemeralInteractionMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.modify.PublicInteractionMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.entities.messages.EphemeralMessage
import net.perfectdreams.discordinteraktions.common.entities.messages.Message
import net.perfectdreams.discordinteraktions.common.entities.messages.PublicMessage

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
    abstract suspend fun sendPublicMessage(message: PublicInteractionOrFollowupMessageCreateBuilder): PublicMessage

    /**
     * The usual way of sending messages to a specific channel/user.
     */
    abstract suspend fun sendEphemeralMessage(message: EphemeralInteractionOrFollowupMessageCreateBuilder): EphemeralMessage

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
    abstract suspend fun updateMessage(message: PublicInteractionMessageModifyBuilder): Message

    /**
     * The usual way of editing a message to a specific channel/user.
     */
    abstract suspend fun updateEphemeralMessage(message: EphemeralInteractionMessageModifyBuilder): Message
}
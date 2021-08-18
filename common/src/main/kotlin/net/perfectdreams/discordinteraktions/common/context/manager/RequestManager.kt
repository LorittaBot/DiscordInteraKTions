package net.perfectdreams.discordinteraktions.common.context.manager

import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.entities.Message
import net.perfectdreams.discordinteraktions.common.utils.InteractionMessage

abstract class RequestManager(val bridge: RequestBridge) {
    /**
     * A deferred response is the one that you can use to
     * be able to edit the original message for 15 minutes since it was sent.
     *
     * The user will just see a loading status for the interaction.
     */
    abstract suspend fun deferMessage(isEphemeral: Boolean)

    /**
     * The usual way of sending messages to a specific channel/user.
     */
    abstract suspend fun sendMessage(message: InteractionMessage): Message
}
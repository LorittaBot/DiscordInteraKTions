package net.perfectdreams.discordinteraktions.context

import net.perfectdreams.discordinteraktions.api.entities.Message
import net.perfectdreams.discordinteraktions.utils.InteractionMessage

interface RequestManager {

    /**
     * A deferred response is the one that you can use to
     * be able to edit the original message for 15 minutes since it was sent.
     *
     * The user will just see a loading status for the interaction.
     */
    suspend fun defer()

    /**
     * The usual way of sending messages to a specific channel/
     * user.
     */
    suspend fun sendMessage(message: InteractionMessage): Message
}
package net.perfectdreams.discordinteraktions.context

import net.perfectdreams.discordinteraktions.api.entities.Message
import net.perfectdreams.discordinteraktions.utils.InteractionMessage

interface RequestManager {
    suspend fun defer()
    suspend fun sendMessage(message: InteractionMessage): Message
}
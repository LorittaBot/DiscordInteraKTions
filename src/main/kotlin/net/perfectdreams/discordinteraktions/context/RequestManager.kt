package net.perfectdreams.discordinteraktions.context

import net.perfectdreams.discordinteraktions.entities.Message
import net.perfectdreams.discordinteraktions.entities.MessageToBeSent

interface RequestManager {
    suspend fun defer()
    suspend fun sendMessage(content: String) = sendMessage(MessageToBeSent(content))
    suspend fun sendMessage(message: MessageToBeSent): Message
}
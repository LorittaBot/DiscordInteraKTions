package net.perfectdreams.discordinteraktions

import dev.kord.common.entity.DiscordInteraction
import io.ktor.application.*
import net.perfectdreams.discordinteraktions.entities.CommandInteraction
import net.perfectdreams.discordinteraktions.entities.Interaction
import net.perfectdreams.discordinteraktions.entities.PingInteraction

abstract class InteractionRequestHandler {
    suspend fun onRequest(call: ApplicationCall, request: Interaction) {
        when (request) {
            is PingInteraction -> onPing(call, request)
            is CommandInteraction -> onCommand(call, request)
        }
    }

    open suspend fun onPing(call: ApplicationCall, request: PingInteraction) {}
    open suspend fun onCommand(call: ApplicationCall, request: CommandInteraction) {}
}
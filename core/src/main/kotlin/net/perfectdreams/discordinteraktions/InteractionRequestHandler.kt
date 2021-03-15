package net.perfectdreams.discordinteraktions

import io.ktor.application.*
import net.perfectdreams.discordinteraktions.entities.CommandInteraction
import net.perfectdreams.discordinteraktions.entities.Interaction
import net.perfectdreams.discordinteraktions.entities.PingInteraction

/**
 * Abstract class that can be inherited to change
 * the way of handling the requests.
 *
 * The server's Interaction Request Handler can be changed on the
 * [InteractionsServer]
 */
abstract class InteractionRequestHandler {
    /**
     * We use this method to separate requests by their
     * interaction type and call their respective methods.
     *
     * @param call The request data
     * @param request The interaction we'll handle
     */
    suspend fun onRequest(call: ApplicationCall, request: Interaction) {
        when (request) {
            is PingInteraction -> onPing(call, request)
            is CommandInteraction -> onCommand(call, request)
        }
    }

    /**
     * Method called when we receive an interaction of the
     * [PingInteraction] type.
     *
     * @param call The Ktor call containing the request details.
     * @param request The interaction data.
     */
    open suspend fun onPing(call: ApplicationCall, request: PingInteraction) {}

    /**
     * Method called when we receive an interaction of the
     * [CommandInteraction] type.
     *
     * @param call The Ktor call containing the request details.
     * @param request The interaction data.
     */
    open suspend fun onCommand(call: ApplicationCall, request: CommandInteraction) {}
}
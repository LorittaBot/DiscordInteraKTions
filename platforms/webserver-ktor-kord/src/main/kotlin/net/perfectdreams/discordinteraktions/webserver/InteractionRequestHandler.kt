package net.perfectdreams.discordinteraktions.webserver

import dev.kord.common.entity.DiscordInteraction
import io.ktor.application.*

/**
 * Abstract class that can be inherited to change
 * the way of handling the requests.
 *
 * The server's Interaction Request Handler can be changed on the
 * [InteractionsServer]
 */
abstract class InteractionRequestHandler {
    /**
     * Method called when we receive an interaction of the
     * [PingInteraction] type.
     *
     * @param call The Ktor call containing the request details.
     * @param request The interaction data.
     */
    open suspend fun onPing(call: ApplicationCall) {}

    /**
     * Method called when we receive an interaction of the
     * Application Command type.
     *
     * @param call The Ktor call containing the request details.
     * @param request The interaction data.
     */
    open suspend fun onCommand(call: ApplicationCall, request: DiscordInteraction) {}

    /**
     * Method called when we receive an interaction of the
     * Component type.
     *
     * @param call The Ktor call containing the request details.
     * @param request The interaction data.
     */
    open suspend fun onComponent(call: ApplicationCall, request: DiscordInteraction) {}

    /**
     * Method called when we receive an interaction of the
     * AutoComplete type.
     *
     * @param call The Ktor call containing the request details.
     * @param request The interaction data.
     */
    open suspend fun onAutocomplete(call: ApplicationCall, request: DiscordInteraction) {}

    /**
     * Method called when we receive an interaction of the
     * ModalSubmit type.
     *
     * @param call The Ktor call containing the request details.
     * @param request The interaction data.
     */
    open suspend fun onModalSubmit(call: ApplicationCall, request: DiscordInteraction) {}
}
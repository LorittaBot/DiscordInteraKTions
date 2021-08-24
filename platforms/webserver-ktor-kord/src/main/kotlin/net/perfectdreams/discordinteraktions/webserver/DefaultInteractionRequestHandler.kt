package net.perfectdreams.discordinteraktions.webserver

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.InteractionResponseType
import dev.kord.common.entity.Snowflake
import dev.kord.rest.service.RestClient
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.common.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.utils.Observable
import net.perfectdreams.discordinteraktions.platforms.kord.utils.KordCommandChecker
import net.perfectdreams.discordinteraktions.webserver.context.manager.WebServerRequestManager

/**
 * This is the default implementation of [InteractionRequestHandler],
 * and the server's built-in request handler too (you can change that on the [InteractionsServer] class)
 *
 * @param m The server that we'll handle the requests for.
 */
@OptIn(KordPreview::class, ExperimentalCoroutinesApi::class)
class DefaultInteractionRequestHandler(
    val applicationId: Snowflake,
    val commandManager: CommandManager,
    val rest: RestClient
) : InteractionRequestHandler() {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    private val kordCommandChecker = KordCommandChecker(commandManager)

    /**
     * Method called when we receive an interaction of the
     * [PingInteraction] type.
     *
     * We'll basically handle this by logging the event
     * and answering with a "Pong!" response.
     *
     * @param call The Ktor call containing the request details.
     * @param request The interaction data.
     */
    override suspend fun onPing(call: ApplicationCall) {
        logger.info { "Ping Request Received!" }
        call.respondText(
            buildJsonObject {
                put("type", InteractionResponseType.Pong.type)
            }.toString(),
            ContentType.Application.Json
        )
    }

    /**
     * Method called when we receive an interaction of the
     * [CommandInteraction] type.
     *
     * We'll basically handle this by logging the event
     * and executing the requested command.
     *
     * @param call The Ktor call containing the request details.
     * @param request The interaction data.
     */
    override suspend fun onCommand(call: ApplicationCall, request: DiscordInteraction) {
        val observableState = Observable(InteractionRequestState.NOT_REPLIED_YET)
        val bridge = RequestBridge(observableState)

        val requestManager = WebServerRequestManager(
            bridge,
            rest,
            applicationId,
            request.token,
            request,
            call
        )

        bridge.manager = requestManager

        kordCommandChecker.checkAndExecute(
            request,
            requestManager
        )

        observableState.awaitChange()
        logger.info { "State was changed to ${observableState.value}, so this means we already replied via the Web Server! Leaving request scope..." }
    }
}
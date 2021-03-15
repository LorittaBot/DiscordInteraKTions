package net.perfectdreams.discordinteraktions

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.InteractionResponseType
import dev.kord.common.entity.Snowflake
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.receiveOrNull
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.context.GuildSlashCommandContext
import net.perfectdreams.discordinteraktions.context.HttpRequestManager
import net.perfectdreams.discordinteraktions.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.context.RequestBridge
import net.perfectdreams.discordinteraktions.context.SlashCommandContext
import net.perfectdreams.discordinteraktions.context.WebServerRequestManager
import net.perfectdreams.discordinteraktions.entities.CommandInteraction
import net.perfectdreams.discordinteraktions.entities.PingInteraction
import net.perfectdreams.discordinteraktions.utils.Observable
import kotlin.properties.ObservableProperty

/**
 * This is the default implementation of [InteractionRequestHandler],
 * and the server's built-in request handler too (you can change that on the [InteractionsServer] class)
 *
 * @param m The server that we'll handle the requests for.
 */
@OptIn(KordPreview::class, ExperimentalCoroutinesApi::class)
class DefaultInteractionRequestHandler(val m: InteractionsServer) : InteractionRequestHandler() {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

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
    override suspend fun onPing(call: ApplicationCall, request: PingInteraction) {
        logger.info { "Ping Request Received! Triggered by ${request.user}" }
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
    override suspend fun onCommand(call: ApplicationCall, request: CommandInteraction) {
        println(request.data.name)

        val command = m.commandManager.commands.first { it.declaration.name == request.data.name }

        println("Command: $command")

        val observableState = Observable(InteractionRequestState.NOT_REPLIED_YET)
        val bridge = RequestBridge(observableState)

        val requestManager = WebServerRequestManager(
            bridge,
            m.rest,
            Snowflake(m.applicationId),
            request.token,
            request,
            call
        )

        bridge.manager = requestManager

        val commandContext = if (request.guildId.value != null) {
            GuildSlashCommandContext(
                request,
                bridge
            )
        } else {
            SlashCommandContext(
                request,
                bridge
            )
        }

        GlobalScope.launch {
            command.executes(commandContext)
            println("Finished execution!")
        }

        observableState.awaitChange()
        logger.info { "State was changed to ${observableState.value}, so this means we already replied via the Web Server! Leaving request scope..." }
    }
}
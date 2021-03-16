package net.perfectdreams.discordinteraktions

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.InteractionResponseType
import dev.kord.common.entity.Snowflake
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.context.GuildSlashCommandContext
import net.perfectdreams.discordinteraktions.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.context.RequestBridge
import net.perfectdreams.discordinteraktions.context.SlashCommandContext
import net.perfectdreams.discordinteraktions.context.WebServerRequestManager
import net.perfectdreams.discordinteraktions.entities.CommandInteraction
import net.perfectdreams.discordinteraktions.entities.PingInteraction
import net.perfectdreams.discordinteraktions.utils.CommandDeclarationUtils
import net.perfectdreams.discordinteraktions.utils.Observable

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

        // Processing subcommands is kinda hard, but not impossible!
        val commandLabels = CommandDeclarationUtils.findAllSubcommandDeclarationNames(request)
        val relativeOptions = CommandDeclarationUtils.getNestedOptions(request.data.options.value)

        println("Subcommand Labels: $commandLabels; Root Options: $relativeOptions")

        val command = m.commandManager.commands.first {
            // This is very complex because this is the *advanced* stuff to check if the subcommand and command group matches
            // First we need to get the root declaration and try following the labels until we find our declaration (or not!)
            val rootDeclaration = it.rootDeclaration

            CommandDeclarationUtils.areLabelsConnectedToCommandDeclaration(
                commandLabels,
                rootDeclaration,
                it.declaration
            )
        }

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
                command,
                request,
                relativeOptions,
                bridge
            )
        } else {
            SlashCommandContext(
                command,
                request,
                relativeOptions,
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
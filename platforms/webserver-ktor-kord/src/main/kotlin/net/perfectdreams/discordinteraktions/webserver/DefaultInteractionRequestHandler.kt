package net.perfectdreams.discordinteraktions.webserver

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.CommandArgument
import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.InteractionResponseType
import dev.kord.common.entity.Option
import dev.kord.common.entity.Snowflake
import dev.kord.rest.service.RestClient
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.common.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.context.commands.GuildSlashCommandContext
import net.perfectdreams.discordinteraktions.common.context.commands.SlashCommandArguments
import net.perfectdreams.discordinteraktions.common.context.commands.SlashCommandContext
import net.perfectdreams.discordinteraktions.common.utils.Observable
import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandExecutorDeclaration
import net.perfectdreams.discordinteraktions.declarations.slash.options.CommandOption
import net.perfectdreams.discordinteraktions.platforms.kord.commands.CommandDeclarationUtils
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordMember
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordUser
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
        println(request.data.name)

        // Processing subcommands is kinda hard, but not impossible!
        val commandLabels = CommandDeclarationUtils.findAllSubcommandDeclarationNames(request)
        val relativeOptions = CommandDeclarationUtils.getNestedOptions(request.data.options.value)

        println("Subcommand Labels: $commandLabels; Root Options: $relativeOptions")

        val command = commandManager.declarations
            .asSequence()
            .mapNotNull {
                CommandDeclarationUtils.getLabelsConnectedToCommandDeclaration(
                    commandLabels,
                    it
                )
            }
            .first()

        val executorDeclaration = command.executor ?: return
        val executor = commandManager.executors.first {
            it.signature() == executorDeclaration.parent
        }

        // Convert the Nested Options into a map, then we can access them with our Discord InteraKTion options!
        val arguments = convertOptions(
            request,
            executorDeclaration,
            relativeOptions ?: listOf()
        )

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

        val kordUser = KordUser(request.member.value?.user?.value ?: request.user.value ?: error("oh no"))
        val guildId = request.guildId.value?.let { net.perfectdreams.discordinteraktions.api.entities.Snowflake(it.value) }

        // If the guild ID is not null, then it means that the interaction happened in a guild!
        val commandContext = if (guildId != null) {
            val kordMember = KordMember(
                request.member.value!! // Should NEVER be null!
            )

            GuildSlashCommandContext(
                bridge,
                kordUser,
                guildId,
                kordMember
            )
        } else {
            SlashCommandContext(
                bridge,
                KordUser(
                    request.member.value?.user?.value ?: request.user.value ?: error("oh no")
                )
            )
        }

        GlobalScope.launch {
            executor.execute(
                commandContext,
                SlashCommandArguments(
                    arguments
                )
            )
            println("Finished execution!")
        }

        observableState.awaitChange()
        logger.info { "State was changed to ${observableState.value}, so this means we already replied via the Web Server! Leaving request scope..." }
    }

    fun convertOptions(request: DiscordInteraction, executorDeclaration: SlashCommandExecutorDeclaration, relativeOptions: List<Option>): Map<CommandOption<*>, Any?> {
        val arguments = mutableMapOf<CommandOption<*>, Any?>()

        for (option in relativeOptions) {
            val interaKTionOption = executorDeclaration.options.arguments
                .firstOrNull { it.name == option.name } ?: continue

            val argument = option as CommandArgument<*>

            arguments[interaKTionOption] = convertOption(
                interaKTionOption,
                argument,
                request
            )
        }

        return arguments
    }

    private fun convertOption(interaKTionOption: CommandOption<*>, argument: CommandArgument<*>, request: DiscordInteraction): Any? {
        println(interaKTionOption.type)
        println(argument.value)

        return when (interaKTionOption.type) {
            /* CommandOptionType.User, CommandOptionType.NullableUser -> {
                val userId = argument.value.value as String

                val resolved = request.data.resolved.value ?: return null
                val resolvedMap = resolved.users.value ?: return null
                val kordInstance = resolvedMap[Snowflake(userId)] ?: return null

                // Now we need to wrap the kord user in our own implementation!
                return KordUser(kordInstance)
            }
            CommandOptionType.Channel, CommandOptionType.NullableChannel -> {
                val userId = argument.value.value as String

                val resolved = request.data.resolved.value ?: return null
                val resolvedMap = resolved.channels.value ?: return null
                val kordInstance = resolvedMap[Snowflake(userId)] ?: return null

                // Now we need to wrap the kord user in our own implementation!
                return KordChannel(kordInstance)
            }
            CommandOptionType.Role, CommandOptionType.NullableRole -> {
                val userId = argument.value.value as String

                val resolved = request.data.resolved.value ?: return null
                val resolvedMap = resolved.roles.value ?: return null
                val kordInstance = resolvedMap[Snowflake(userId)] ?: return null

                // Now we need to wrap the kord user in our own implementation!
                return KordRole(kordInstance)
            } */
            else -> { argument.value }
        }
    }
}
package net.perfectdreams.discordinteraktions.kord

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.CommandArgument
import dev.kord.common.entity.Option
import dev.kord.common.entity.Snowflake
import dev.kord.gateway.Gateway
import dev.kord.gateway.InteractionCreate
import dev.kord.gateway.on
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.perfectdreams.discordinteraktions.commands.CommandManager
import net.perfectdreams.discordinteraktions.commands.SlashCommandArguments
import net.perfectdreams.discordinteraktions.context.GuildSlashCommandContext
import net.perfectdreams.discordinteraktions.context.manager.InitialHttpRequestManager
import net.perfectdreams.discordinteraktions.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.context.RequestBridge
import net.perfectdreams.discordinteraktions.context.SlashCommandContext
import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandExecutorDeclaration
import net.perfectdreams.discordinteraktions.declarations.slash.options.CommandOption
import net.perfectdreams.discordinteraktions.declarations.slash.options.CommandOptionType
import net.perfectdreams.discordinteraktions.entities.CommandInteraction
import net.perfectdreams.discordinteraktions.internal.entities.KordChannel
import net.perfectdreams.discordinteraktions.internal.entities.KordRole
import net.perfectdreams.discordinteraktions.internal.entities.KordUser
import net.perfectdreams.discordinteraktions.utils.CommandDeclarationUtils
import net.perfectdreams.discordinteraktions.utils.Observable

@KordPreview
fun Gateway.installDiscordInteraKTions(
    commandManager: CommandManager
) {
    on<InteractionCreate> {
        val interactionData = this.interaction

        // Convert to InteraKTions objects
        val request = CommandInteraction(
            interactionData.id,
            interactionData.type,
            interactionData.data,
            interactionData.guildId,
            interactionData.channelId,
            interactionData.member,
            interactionData.user,
            interactionData.token,
            interactionData.version
        )

        println(request)

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

        val executorDeclaration = command.executor ?: return@on
        val executor = commandManager.executors.first {
            it::class == executorDeclaration.parent
        }

        // Convert the Nested Options into a map, then we can access them with our Discord InteraKTion options!
        val arguments = convertOptions(
            request,
            executorDeclaration,
            relativeOptions ?: listOf()
        )

        val observableState = Observable(InteractionRequestState.NOT_REPLIED_YET)
        val bridge = RequestBridge(observableState)

        val requestManager = InitialHttpRequestManager(
            bridge,
            commandManager.rest,
            commandManager.applicationId,
            request.token,
            request
        )

        bridge.manager = requestManager

        val commandContext = if (request.guildId.value != null) {
            GuildSlashCommandContext(
                request,
                relativeOptions,
                bridge
            )
        } else {
            SlashCommandContext(
                request,
                relativeOptions,
                bridge
            )
        }

        GlobalScope.launch {
            executor.execute(
                commandContext,
                SlashCommandArguments(
                    arguments
                )
            )
        }
    }
}


private fun convertOptions(request: CommandInteraction, executorDeclaration: SlashCommandExecutorDeclaration, relativeOptions: List<Option>): Map<CommandOption<*>, Any?> {
    val arguments = mutableMapOf<CommandOption<*>, Any?>()

    for (option in relativeOptions) {
        val interaKTionOption = executorDeclaration.options.arguments
            .firstOrNull { it.name == option.name } ?: continue

        val argument = option as CommandArgument

        arguments[interaKTionOption] = convertOption(
            interaKTionOption,
            argument,
            request
        )
    }

    return arguments
}

private fun convertOption(interaKTionOption: CommandOption<*>, argument: CommandArgument, request: CommandInteraction): Any? {
    return when (interaKTionOption.type) {
        CommandOptionType.User, CommandOptionType.NullableUser -> {
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
        }
        else -> { argument.value.value }
    }
}
package net.perfectdreams.discordinteraktions.platforms.kord

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.CommandArgument
import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Option
import dev.kord.common.entity.Snowflake
import dev.kord.gateway.Gateway
import dev.kord.gateway.InteractionCreate
import dev.kord.gateway.on
import dev.kord.rest.service.RestClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.common.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.context.commands.GuildSlashCommandContext
import net.perfectdreams.discordinteraktions.common.context.commands.SlashCommandArguments
import net.perfectdreams.discordinteraktions.common.context.commands.SlashCommandContext
import net.perfectdreams.discordinteraktions.common.utils.Observable
import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandExecutorDeclaration
import net.perfectdreams.discordinteraktions.declarations.slash.options.CommandOption
import net.perfectdreams.discordinteraktions.platforms.kord.context.manager.InitialHttpRequestManager
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordMember
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordUser
import net.perfectdreams.discordinteraktions.platforms.kord.commands.CommandDeclarationUtils
import net.perfectdreams.discordinteraktions.platforms.kord.utils.toKordSnowflake

@KordPreview
fun Gateway.installDiscordInteraKTions(
    applicationId: net.perfectdreams.discordinteraktions.api.entities.Snowflake,
    rest: RestClient,
    commandManager: CommandManager
) {
    on<InteractionCreate> {
        val request = this.interaction
        println("interaction type: ${request.type}")

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
            rest,
            applicationId.toKordSnowflake(),
            request.token,
            request
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
        }
    }
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
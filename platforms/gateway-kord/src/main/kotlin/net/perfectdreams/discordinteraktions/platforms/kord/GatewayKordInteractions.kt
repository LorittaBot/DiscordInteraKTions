package net.perfectdreams.discordinteraktions.platforms.kord

import dev.kord.common.annotation.KordPreview
import dev.kord.gateway.Gateway
import dev.kord.gateway.InteractionCreate
import dev.kord.gateway.on
import dev.kord.rest.service.RestClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.common.commands.slash.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.context.commands.GuildApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.context.commands.ChatCommandArguments
import net.perfectdreams.discordinteraktions.common.context.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData
import net.perfectdreams.discordinteraktions.common.utils.Observable
import net.perfectdreams.discordinteraktions.declarations.commands.SlashCommandDeclaration
import net.perfectdreams.discordinteraktions.platforms.kord.context.manager.InitialHttpRequestManager
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordMember
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordUser
import net.perfectdreams.discordinteraktions.platforms.kord.commands.CommandDeclarationUtils
import net.perfectdreams.discordinteraktions.platforms.kord.utils.toDiscordInteraKTionsResolvedObjects
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
            .filterIsInstance<SlashCommandDeclaration>() // We only care about Slash Command Declarations here
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
        } as SlashCommandExecutor

        // Convert the Nested Options into a map, then we can access them with our Discord InteraKTion options!
        val arguments = CommandDeclarationUtils.convertOptions(
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

        val interactionData = InteractionData(request.data.resolved.value?.toDiscordInteraKTionsResolvedObjects())

        // If the guild ID is not null, then it means that the interaction happened in a guild!
        val commandContext = if (guildId != null) {
            val kordMember = KordMember(
                request.member.value!! // Should NEVER be null!
            )

            GuildApplicationCommandContext(
                bridge,
                kordUser,
                interactionData,
                guildId,
                kordMember
            )
        } else {
            ApplicationCommandContext(
                bridge,
                KordUser(
                    request.member.value?.user?.value ?: request.user.value ?: error("oh no")
                ),
                interactionData
            )
        }

        GlobalScope.launch {
            executor.execute(
                commandContext,
                ChatCommandArguments(
                    arguments
                )
            )
        }
    }
}
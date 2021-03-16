package net.perfectdreams.discordinteraktions.kord

import dev.kord.common.annotation.KordPreview
import dev.kord.gateway.Gateway
import dev.kord.gateway.InteractionCreate
import dev.kord.gateway.on
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.perfectdreams.discordinteraktions.commands.CommandManager
import net.perfectdreams.discordinteraktions.context.GuildSlashCommandContext
import net.perfectdreams.discordinteraktions.context.InitialHttpRequestManager
import net.perfectdreams.discordinteraktions.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.context.RequestBridge
import net.perfectdreams.discordinteraktions.context.SlashCommandContext
import net.perfectdreams.discordinteraktions.entities.CommandInteraction
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

        val command = commandManager.commands.first {
            // This is very complex because this is the *advanced* stuff to check if the subcommand and command group matches
            // First we need to get the root declaration and try following the labels until we find our declaration (or not!)
            val rootDeclaration = it.rootDeclaration

            CommandDeclarationUtils.areLabelsConnectedToCommandDeclaration(
                commandLabels,
                rootDeclaration,
                it.declaration
            )
        }

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
        }
    }
}
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

        val command = commandManager.commands.first { it.declaration.name == request.data.name }

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
        }
    }
}
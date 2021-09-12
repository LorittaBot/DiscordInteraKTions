package net.perfectdreams.discordinteraktions.platforms.kord

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.InteractionType
import dev.kord.common.entity.Snowflake
import dev.kord.gateway.Gateway
import dev.kord.gateway.InteractionCreate
import dev.kord.gateway.on
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.common.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.utils.Observable
import net.perfectdreams.discordinteraktions.platforms.kord.context.manager.InitialHttpRequestManager
import net.perfectdreams.discordinteraktions.platforms.kord.utils.KordCommandChecker
import net.perfectdreams.discordinteraktions.platforms.kord.utils.KordComponentChecker

@KordPreview
fun Gateway.installDiscordInteraKTions(
    applicationId: Snowflake,
    rest: RestClient,
    commandManager: CommandManager
) {
    val kordCommandChecker = KordCommandChecker(commandManager)
    val kordComponentChecker = KordComponentChecker(commandManager)

    on<InteractionCreate> {
        val request = this.interaction

        val observableState = Observable(InteractionRequestState.NOT_REPLIED_YET)
        val bridge = RequestBridge(observableState)

        val requestManager = InitialHttpRequestManager(
            bridge,
            rest,
            applicationId,
            request.token,
            request
        )

        bridge.manager = requestManager

        if (request.type == InteractionType.ApplicationCommand)
            kordCommandChecker.checkAndExecute(
                request,
                requestManager
            )
        else if (request.type == InteractionType.Component) {
            kordComponentChecker.checkAndExecute(
                request,
                requestManager
            )
        }
    }
}
package net.perfectdreams.discordinteraktions.platforms.kord

import dev.kord.common.entity.InteractionType
import dev.kord.common.entity.Snowflake
import dev.kord.gateway.Gateway
import dev.kord.gateway.InteractionCreate
import dev.kord.gateway.on
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.common.requests.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.requests.RequestBridge
import net.perfectdreams.discordinteraktions.common.utils.Observable
import net.perfectdreams.discordinteraktions.common.requests.managers.InitialHttpRequestManager
import net.perfectdreams.discordinteraktions.platforms.kord.utils.KordAutocompleteChecker
import net.perfectdreams.discordinteraktions.platforms.kord.utils.KordCommandChecker
import net.perfectdreams.discordinteraktions.platforms.kord.utils.KordComponentChecker

fun Gateway.installDiscordInteraKTions(
    applicationId: Snowflake,
    rest: RestClient,
    commandManager: CommandManager
) {
    val kordCommandChecker = KordCommandChecker(commandManager)
    val kordComponentChecker = KordComponentChecker(commandManager)
    val kordAutocompleteChecker = KordAutocompleteChecker(commandManager)

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
        else if (request.type == InteractionType.Component)
            kordComponentChecker.checkAndExecute(
                request,
                requestManager
            )
        else if (request.type == InteractionType.AutoComplete)
            kordAutocompleteChecker.checkAndExecute(
                request,
                requestManager
            )
    }
}
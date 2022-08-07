package net.perfectdreams.discordinteraktions.platforms.kord

import dev.kord.common.entity.InteractionType
import dev.kord.common.entity.Snowflake
import dev.kord.gateway.Gateway
import dev.kord.gateway.InteractionCreate
import dev.kord.gateway.on
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.DiscordInteraKTions
import net.perfectdreams.discordinteraktions.common.commands.InteractionsManager
import net.perfectdreams.discordinteraktions.common.requests.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.requests.RequestBridge
import net.perfectdreams.discordinteraktions.common.utils.Observable
import net.perfectdreams.discordinteraktions.common.requests.managers.InitialHttpRequestManager
import net.perfectdreams.discordinteraktions.platforms.kord.utils.KordAutocompleteChecker
import net.perfectdreams.discordinteraktions.platforms.kord.utils.KordCommandChecker
import net.perfectdreams.discordinteraktions.platforms.kord.utils.KordComponentChecker
import net.perfectdreams.discordinteraktions.platforms.kord.utils.KordModalChecker

fun Gateway.installDiscordInteraKTions(interaKTions: DiscordInteraKTions) {
    on<InteractionCreate> {
        val request = this.interaction

        val observableState = Observable(InteractionRequestState.NOT_REPLIED_YET)
        val bridge = RequestBridge(observableState)

        val requestManager = InitialHttpRequestManager(
            bridge,
            interaKTions.kord,
            interaKTions.applicationId,
            request.id,
            request.token
        )

        bridge.manager = requestManager

        when (request.type) {
            InteractionType.ApplicationCommand -> interaKTions.commandChecker.checkAndExecute(
                request,
                requestManager
            )
            InteractionType.Component -> interaKTions.componentChecker.checkAndExecute(
                request,
                requestManager
            )
            InteractionType.AutoComplete -> interaKTions.autocompleteChecker.checkAndExecute(
                request,
                requestManager
            )
            InteractionType.ModalSubmit -> interaKTions.modalChecker.checkAndExecute(
                request,
                requestManager
            )
            else -> {}
        }
    }
}
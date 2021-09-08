package net.perfectdreams.discordinteraktions.platforms.kord

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.InteractionType
import dev.kord.common.entity.Snowflake
import dev.kord.gateway.Gateway
import dev.kord.gateway.InteractionCreate
import dev.kord.gateway.on
import dev.kord.rest.service.RestClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.common.components.buttons.ButtonClickWithDataExecutor
import net.perfectdreams.discordinteraktions.common.components.buttons.ButtonClickWithNoDataExecutor
import net.perfectdreams.discordinteraktions.common.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.context.buttons.ButtonClickContext
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData
import net.perfectdreams.discordinteraktions.common.utils.Observable
import net.perfectdreams.discordinteraktions.platforms.kord.context.manager.InitialHttpRequestManager
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordUser
import net.perfectdreams.discordinteraktions.platforms.kord.entities.messages.KordPublicMessage
import net.perfectdreams.discordinteraktions.platforms.kord.utils.KordCommandChecker
import net.perfectdreams.discordinteraktions.platforms.kord.utils.toDiscordInteraKTionsResolvedObjects

@KordPreview
fun Gateway.installDiscordInteraKTions(
    applicationId: Snowflake,
    rest: RestClient,
    commandManager: CommandManager
) {
    val kordCommandChecker = KordCommandChecker(commandManager)

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
            // If the button doesn't have a custom ID, we won't process it
            val buttonCustomId = request.data.customId.value ?: return@on

            val executorId = buttonCustomId.substringBefore(":")
            val data = buttonCustomId.substringAfter(":")

            val buttonExecutorDeclaration = commandManager.buttonDeclarations
                .asSequence()
                .filter {
                    it.id == executorId
                }
                .first()

            val executor = commandManager.buttonExecutors.first {
                it.signature() == buttonExecutorDeclaration.parent
            }

            val kordUser = KordUser(request.member.value?.user?.value ?: request.user.value ?: error("oh no"))
            val guildId = request.guildId.value

            val interactionData = InteractionData(request.data.resolved.value?.toDiscordInteraKTionsResolvedObjects())

            val buttonClickContext = ButtonClickContext(
                bridge,
                kordUser,
                KordPublicMessage(request.message.value!!), // This should NEVER be null if it is a component message
                interactionData
            )

            GlobalScope.launch {
                if (executor is ButtonClickWithNoDataExecutor)
                    executor.onClick(
                        kordUser,
                        buttonClickContext
                    )
                else if (executor is ButtonClickWithDataExecutor)
                    executor.onClick(
                        kordUser,
                        buttonClickContext,
                        data
                    )
            }
        }
    }
}
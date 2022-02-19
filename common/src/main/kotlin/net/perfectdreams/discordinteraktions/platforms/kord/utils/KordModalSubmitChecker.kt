package net.perfectdreams.discordinteraktions.platforms.kord.utils

import dev.kord.common.entity.ComponentType
import dev.kord.common.entity.DiscordComponent
import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.DiscordModalComponent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData
import net.perfectdreams.discordinteraktions.common.modals.GuildModalSubmitContext
import net.perfectdreams.discordinteraktions.common.modals.ModalSubmitContext
import net.perfectdreams.discordinteraktions.common.modals.ModalSubmitExecutor
import net.perfectdreams.discordinteraktions.common.modals.ModalSubmitWithDataExecutor
import net.perfectdreams.discordinteraktions.common.modals.components.ModalArguments
import net.perfectdreams.discordinteraktions.common.requests.managers.RequestManager
import net.perfectdreams.discordinteraktions.common.utils.InteraKTionsExceptions
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordInteractionMember
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordUser

/**
 * Checks, matches and executes commands, this is a class because we share code between the `gateway-kord` and `webserver-ktor-kord` modules
 */
class KordModalSubmitChecker(val commandManager: CommandManager) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    fun checkAndExecute(request: DiscordInteraction, requestManager: RequestManager) {
        val bridge = requestManager.bridge

        // If the component doesn't have a custom ID, we won't process it
        val componentCustomId = request.data.customId.value ?: return

        val executorId = componentCustomId.substringBefore(":")
        // While we don't share the modal data to the user, it is used to avoid Discord reusing the same form for multiple users
        val data = componentCustomId.substringAfter(":")

        logger.debug { request.data.name }

        val kordUser = KordUser(request.member.value?.user?.value ?: request.user.value ?: error("oh no"))

        val guildId = request.guildId.value

        val interactionData = InteractionData(request.data.resolved.value?.toDiscordInteraKTionsResolvedObjects())

        // If the guild ID is not null, then it means that the interaction happened in a guild!
        val modalSubmitContext = if (guildId != null) {
            val member = request.member.value!! // Should NEVER be null!
            val kordMember = KordInteractionMember(
                member,
                KordUser(member.user.value!!) // Also should NEVER be null!
            )

            GuildModalSubmitContext(
                bridge,
                kordUser,
                request.channelId,
                interactionData,
                request,
                guildId,
                kordMember
            )
        } else {
            ModalSubmitContext(
                bridge,
                kordUser,
                request.channelId,
                interactionData,
                request
            )
        }

        val modalSubmitDeclaration = commandManager.modalSubmitDeclarations.firstOrNull { it.id == executorId } ?: InteraKTionsExceptions.missingDeclaration("modal submit")
        val modalSubmitExecutor = commandManager.modalSubmitExecutors.firstOrNull { it.signature() == modalSubmitDeclaration.parent } ?: InteraKTionsExceptions.missingExecutor("modal submit")
        val allComponentsInRequest = request.data.components.value ?: error("Missing component list for the modal submit request!")
        val componentsFlatMap = mutableListOf<DiscordComponent>()
        allComponentsInRequest.forEach {
            recursiveComponentFlatMap(it, componentsFlatMap)
        }

        val textInputComponents = componentsFlatMap.filter { it.type == ComponentType.TextInput }
            .filterIsInstance<DiscordModalComponent>()

        val map = textInputComponents.associate {
            modalSubmitDeclaration.options.arguments.first { arg ->
                arg.name == it.customId.value
            } to it.value.value
        }

        GlobalScope.launch {
            when (modalSubmitExecutor) {
                is ModalSubmitExecutor -> {
                    modalSubmitExecutor.onModalSubmit(
                        modalSubmitContext,
                        ModalArguments(map)
                    )
                }
                is ModalSubmitWithDataExecutor -> {
                    modalSubmitExecutor.onModalSubmit(
                        modalSubmitContext,
                        ModalArguments(map),
                        data
                    )
                }
            }
        }
    }

    private fun recursiveComponentFlatMap(component: DiscordComponent, list: MutableList<DiscordComponent>) {
        component.components.value?.forEach {
            list.add(it)
            recursiveComponentFlatMap(it, list)
        }
    }
}
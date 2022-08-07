package net.perfectdreams.discordinteraktions.platforms.kord.utils

import dev.kord.common.entity.ComponentType
import dev.kord.common.entity.DiscordComponent
import dev.kord.common.entity.DiscordInteraction
import dev.kord.core.Kord
import dev.kord.core.cache.data.MemberData
import dev.kord.core.cache.data.UserData
import dev.kord.core.entity.Member
import dev.kord.core.entity.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.common.commands.InteractionsManager
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData
import net.perfectdreams.discordinteraktions.common.modals.GuildModalContext
import net.perfectdreams.discordinteraktions.common.modals.ModalContext
import net.perfectdreams.discordinteraktions.common.modals.components.ModalArguments
import net.perfectdreams.discordinteraktions.common.requests.managers.RequestManager
import net.perfectdreams.discordinteraktions.common.utils.InteraKTionsExceptions

/**
 * Checks, matches and executes commands, this is a class because we share code between the `gateway-kord` and `webserver-ktor-kord` modules
 */
class KordModalChecker(val kord: Kord, val interactionsManager: InteractionsManager) {
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

        val kordUser = User(
            UserData.from(request.member.value?.user?.value ?: request.user.value ?: error("oh no")),
            kord
        )

        val guildId = request.guildId.value

        val interactionData = InteractionData(request.data.resolved.value?.toDiscordInteraKTionsResolvedObjects(kord, guildId))

        val modalSubmitDeclaration = interactionsManager.modalDeclarations.firstOrNull { it.id == executorId } ?: InteraKTionsExceptions.missingDeclaration("modal submit")

        // If the guild ID is not null, then it means that the interaction happened in a guild!
        val modalContext = if (guildId != null) {
            val kordMember = Member(
                MemberData.from(kordUser.id, guildId, request.member.value!!), // Should NEVER be null!
                kordUser.data,
                kord
            )

            GuildModalContext(
                bridge,
                kordUser,
                request.channelId,
                modalSubmitDeclaration,
                data.ifEmpty { null },
                interactionData,
                request,
                guildId,
                kordMember
            )
        } else {
            ModalContext(
                bridge,
                kordUser,
                request.channelId,
                modalSubmitDeclaration,
                data.ifEmpty { null },
                interactionData,
                request
            )
        }

        val modalExecutor = interactionsManager.modalExecutors.firstOrNull { it.signature() == modalSubmitDeclaration.parent } ?: InteraKTionsExceptions.missingExecutor("modal submit")
        val allComponentsInRequest = request.data.components.value ?: error("Missing component list for the modal submit request!")
        val componentsFlatMap = mutableListOf<DiscordComponent>()
        allComponentsInRequest.forEach {
            recursiveComponentFlatMap(it, componentsFlatMap)
        }

        val textInputComponents = componentsFlatMap.filter { it.type == ComponentType.TextInput }

        val map = textInputComponents.associate {
            (modalSubmitDeclaration.options.references.firstOrNull { arg ->
                arg.customId == it.customId.value
            } ?: error("I couldn't find a matching ModalComponent named ${it.customId.value} in the modal executor declaration!")) to it.value.value
        }

        GlobalScope.launch {
            modalExecutor.onSubmit(
                modalContext,
                ModalArguments(map)
            )
        }
    }

    private fun recursiveComponentFlatMap(component: DiscordComponent, list: MutableList<DiscordComponent>) {
        component.components.value?.forEach {
            list.add(it)
            recursiveComponentFlatMap(it, list)
        }
    }
}
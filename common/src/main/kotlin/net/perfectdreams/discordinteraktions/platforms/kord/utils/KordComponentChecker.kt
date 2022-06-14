package net.perfectdreams.discordinteraktions.platforms.kord.utils

import dev.kord.common.entity.ComponentType
import dev.kord.common.entity.DiscordInteraction
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.common.components.ButtonClickExecutor
import net.perfectdreams.discordinteraktions.common.components.ButtonClickWithDataExecutor
import net.perfectdreams.discordinteraktions.common.components.SelectMenuExecutor
import net.perfectdreams.discordinteraktions.common.components.SelectMenuWithDataExecutor
import net.perfectdreams.discordinteraktions.common.components.ComponentContext
import net.perfectdreams.discordinteraktions.common.components.GuildComponentContext
import net.perfectdreams.discordinteraktions.common.requests.managers.RequestManager
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData
import net.perfectdreams.discordinteraktions.common.utils.InteraKTionsExceptions
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordInteractionMember
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordUser
import net.perfectdreams.discordinteraktions.platforms.kord.entities.messages.KordPublicMessage

/**
 * Checks, matches and executes commands, this is a class because we share code between the `gateway-kord` and `webserver-ktor-kord` modules
 */
class KordComponentChecker(val commandManager: CommandManager) {
    fun checkAndExecute(request: DiscordInteraction, requestManager: RequestManager) {
        val bridge = requestManager.bridge

        val componentType = request.data.componentType.value ?: error("Component Type is not present in Discord's request! Bug?")

        // If the component doesn't have a custom ID, we won't process it
        val componentCustomId = request.data.customId.value ?: return

        val executorId = componentCustomId.substringBefore(":")
        val data = componentCustomId.substringAfter(":")

        val kordUser = KordUser(request.member.value?.user?.value ?: request.user.value ?: error("oh no"))
        val kordPublicMessage = KordPublicMessage(request.message.value!!)

        val guildId = request.guildId.value

        val interactionData = InteractionData(request.data.resolved.value?.toDiscordInteraKTionsResolvedObjects(guildId))

        // If the guild ID is not null, then it means that the interaction happened in a guild!
        val componentContext = if (guildId != null) {
            val member = request.member.value!! // Should NEVER be null!
            val kordMember = KordInteractionMember(
                guildId,
                member,
                KordUser(member.user.value!!) // Also should NEVER be null!
            )

            GuildComponentContext(
                bridge,
                kordUser,
                request.channelId,
                kordPublicMessage,
                interactionData,
                request,
                guildId,
                kordMember
            )
        } else {
            ComponentContext(
                bridge,
                kordUser,
                request.channelId,
                kordPublicMessage,
                interactionData,
                request
            )
        }

        // Now this changes a bit depending on what we are trying to execute
        when (componentType) {
            is ComponentType.Unknown -> error("Unknown Component Type!")
            ComponentType.ActionRow -> error("Received a ActionRow component interaction... but that's impossible!")
            ComponentType.Button -> {
                val buttonExecutorDeclaration = commandManager.buttonDeclarations
                    .asSequence()
                    .filter {
                        it.id == executorId
                    }
                    .firstOrNull() ?: InteraKTionsExceptions.missingDeclaration("button")

                val executor = commandManager.buttonExecutors.firstOrNull {
                    it.signature() == buttonExecutorDeclaration.parent
                } ?: InteraKTionsExceptions.missingExecutor("button")

                GlobalScope.launch {
                    when (executor) {
                        is ButtonClickExecutor -> executor.onClick(
                            kordUser,
                            componentContext
                        )
                        is ButtonClickWithDataExecutor -> executor.onClick(
                            kordUser,
                            componentContext,
                            data
                        )
                    }
                }
            }
            ComponentType.SelectMenu -> {
                val executorDeclaration = commandManager.selectMenusDeclarations
                    .asSequence()
                    .filter {
                        it.id == executorId
                    }
                    .firstOrNull() ?: InteraKTionsExceptions.missingDeclaration("select menu")

                val executor = commandManager.selectMenusExecutors.firstOrNull {
                    it.signature() == executorDeclaration.parent
                } ?: InteraKTionsExceptions.missingExecutor("select menu")

                GlobalScope.launch {
                    when (executor) {
                        is SelectMenuExecutor -> executor.onSelect(
                            kordUser,
                            componentContext,
                            request.data.values.value ?: error("Values list is null!")
                        )
                        is SelectMenuWithDataExecutor -> executor.onSelect(
                            kordUser,
                            componentContext,
                            data,
                            request.data.values.value ?: error("Values list is null!")
                        )
                    }
                }
            }
            ComponentType.TextInput -> TODO() // As far as I know this should NEVER happen here!
        }
    }
}
package net.perfectdreams.discordinteraktions.platforms.kord.utils

import dev.kord.common.entity.ApplicationCommandType
import dev.kord.common.entity.ComponentType
import dev.kord.common.entity.DiscordInteraction
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.common.commands.message.MessageCommandExecutor
import net.perfectdreams.discordinteraktions.common.commands.slash.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.commands.user.UserCommandExecutor
import net.perfectdreams.discordinteraktions.common.components.buttons.ButtonClickWithDataExecutor
import net.perfectdreams.discordinteraktions.common.components.buttons.ButtonClickWithNoDataExecutor
import net.perfectdreams.discordinteraktions.common.components.selects.SelectMenuWithDataExecutor
import net.perfectdreams.discordinteraktions.common.components.selects.SelectMenuWithNoDataExecutor
import net.perfectdreams.discordinteraktions.common.context.InteractionRequestState
import net.perfectdreams.discordinteraktions.common.context.RequestBridge
import net.perfectdreams.discordinteraktions.common.context.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.context.commands.GuildApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.context.commands.slash.SlashCommandArguments
import net.perfectdreams.discordinteraktions.common.context.components.ComponentContext
import net.perfectdreams.discordinteraktions.common.context.components.GuildComponentContext
import net.perfectdreams.discordinteraktions.common.context.manager.RequestManager
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData
import net.perfectdreams.discordinteraktions.common.utils.Observable
import net.perfectdreams.discordinteraktions.declarations.commands.MessageCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.SlashCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.UserCommandDeclaration
import net.perfectdreams.discordinteraktions.platforms.kord.commands.CommandDeclarationUtils
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

        val interactionData = InteractionData(request.data.resolved.value?.toDiscordInteraKTionsResolvedObjects())

        // If the guild ID is not null, then it means that the interaction happened in a guild!
        val componentContext = if (guildId != null) {
            val member = request.member.value!! // Should NEVER be null!
            val kordMember = KordInteractionMember(
                member,
                KordUser(member.user.value!!) // Also should NEVER be null!
            )

            GuildComponentContext(
                bridge,
                kordUser,
                request.channelId,
                kordPublicMessage,
                interactionData,
                guildId,
                kordMember
            )
        } else {
            ComponentContext(
                bridge,
                kordUser,
                request.channelId,
                kordPublicMessage,
                interactionData
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
                    .first()

                val executor = commandManager.buttonExecutors.first {
                    it.signature() == buttonExecutorDeclaration.parent
                }

                GlobalScope.launch {
                    if (executor is ButtonClickWithNoDataExecutor)
                        executor.onClick(
                            kordUser,
                            componentContext
                        )
                    else if (executor is ButtonClickWithDataExecutor)
                        executor.onClick(
                            kordUser,
                            componentContext,
                            data
                        )
                }
            }
            ComponentType.SelectMenu -> {
                val executorDeclaration = commandManager.selectMenusDeclarations
                    .asSequence()
                    .filter {
                        it.id == executorId
                    }
                    .first()

                val executor = commandManager.selectMenusExecutors.first {
                    it.signature() == executorDeclaration.parent
                }

                GlobalScope.launch {
                    if (executor is SelectMenuWithNoDataExecutor)
                        executor.onSelect(
                            kordUser,
                            componentContext,
                            request.data.values.value ?: error("Values list is null!")
                        )
                    else if (executor is SelectMenuWithDataExecutor)
                        executor.onSelect(
                            kordUser,
                            componentContext,
                            data,
                            request.data.values.value ?: error("Values list is null!")
                        )
                }
            }
        }
    }
}
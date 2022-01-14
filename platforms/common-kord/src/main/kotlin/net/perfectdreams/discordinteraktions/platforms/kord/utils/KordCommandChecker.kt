package net.perfectdreams.discordinteraktions.platforms.kord.utils

import dev.kord.common.entity.ApplicationCommandType
import dev.kord.common.entity.DiscordInteraction
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.common.commands.message.MessageCommandExecutor
import net.perfectdreams.discordinteraktions.common.commands.slash.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.commands.user.UserCommandExecutor
import net.perfectdreams.discordinteraktions.common.context.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.context.commands.GuildApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.context.commands.slash.SlashCommandArguments
import net.perfectdreams.discordinteraktions.common.context.manager.RequestManager
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData
import net.perfectdreams.discordinteraktions.common.utils.InteraKTionsExceptions
import net.perfectdreams.discordinteraktions.declarations.commands.MessageCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.SlashCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.UserCommandDeclaration
import net.perfectdreams.discordinteraktions.platforms.kord.commands.CommandDeclarationUtils
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordInteractionMember
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordUser

/**
 * Checks, matches and executes commands, this is a class because we share code between the `gateway-kord` and `webserver-ktor-kord` modules
 */
class KordCommandChecker(val commandManager: CommandManager) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    fun checkAndExecute(request: DiscordInteraction, requestManager: RequestManager) {
        val bridge = requestManager.bridge

        logger.debug { request.data.name }

        // Processing subcommands is kinda hard, but not impossible!
        val commandLabels = CommandDeclarationUtils.findAllSubcommandDeclarationNames(request)
        val relativeOptions = CommandDeclarationUtils.getNestedOptions(request.data.options.value)

        val applicationCommandType = request.data.type.value ?: error("Application Command Type is null, so we don't know what it is going to be used for!")

        val kordUser = KordUser(request.member.value?.user?.value ?: request.user.value ?: error("oh no"))
        val guildId = request.guildId.value

        val interactionData = InteractionData(request.data.resolved.value?.toDiscordInteraKTionsResolvedObjects())

        // If the guild ID is not null, then it means that the interaction happened in a guild!
        val commandContext = if (guildId != null) {
            val member = request.member.value!! // Should NEVER be null!
            val kordMember = KordInteractionMember(
                member,
                KordUser(member.user.value!!) // Also should NEVER be null!
            )

            GuildApplicationCommandContext(
                bridge,
                kordUser,
                request.channelId,
                interactionData,
                guildId,
                kordMember
            )
        } else {
            ApplicationCommandContext(
                bridge,
                KordUser(
                    request.member.value?.user?.value ?: request.user.value ?: error("oh no")
                ),
                request.channelId,
                interactionData
            )
        }

        when (applicationCommandType) {
            is ApplicationCommandType.Unknown -> {
                error("Received unknown command type! ID: ${applicationCommandType.value}")
            }
            ApplicationCommandType.ChatInput -> {
                logger.debug { "Subcommand Labels: $commandLabels; Root Options: $relativeOptions" }

                val command = CommandDeclarationUtils.getApplicationCommandDeclarationFromLabel<SlashCommandDeclaration>(commandManager, commandLabels)
                    ?: InteraKTionsExceptions.missingDeclaration("slash command")

                val executorDeclaration = command.executor ?: return
                val executor = commandManager.executors.firstOrNull {
                    it.signature() == executorDeclaration.parent
                } as SlashCommandExecutor? ?: InteraKTionsExceptions.missingExecutor("slash command")

                // Convert the Nested Options into a map, then we can access them with our Discord InteraKTion options!
                val arguments = CommandDeclarationUtils.convertOptions(
                    request,
                    executorDeclaration,
                    relativeOptions ?: listOf()
                )

                GlobalScope.launch {
                    executor.execute(
                        commandContext,
                        SlashCommandArguments(
                            arguments
                        )
                    )
                    logger.debug { "Finished $applicationCommandType's execution!" }
                }
            }

            ApplicationCommandType.User -> {
                val command = CommandDeclarationUtils.getApplicationCommandDeclarationFromLabel<UserCommandDeclaration>(commandManager, commandLabels)
                    ?: InteraKTionsExceptions.missingDeclaration("user command")

                val executorDeclaration = command.executor
                val executor = commandManager.executors.firstOrNull {
                    it.signature() == executorDeclaration.parent
                } as UserCommandExecutor? ?: InteraKTionsExceptions.missingExecutor("user command")

                val targetUserId = request.data.targetId.value
                val targetUser = interactionData.resolved?.users?.get(targetUserId) ?: error("Target User is null in a User Command! Bug?")
                val targetMember = interactionData.resolved?.members?.get(targetUserId)

                GlobalScope.launch {
                    executor.execute(commandContext, targetUser, targetMember)
                    logger.debug { "Finished $applicationCommandType's execution!" }
                }
            }

            ApplicationCommandType.Message -> {
                val command = CommandDeclarationUtils.getApplicationCommandDeclarationFromLabel<MessageCommandDeclaration>(commandManager, commandLabels)
                    ?: InteraKTionsExceptions.missingDeclaration("message command")

                val executorDeclaration = command.executor
                val executor = commandManager.executors.firstOrNull {
                    it.signature() == executorDeclaration.parent
                } as MessageCommandExecutor? ?: InteraKTionsExceptions.missingExecutor("message command")

                val targetMessageId = request.data.targetId.value
                val targetMessage = interactionData.resolved?.messages?.get(targetMessageId) ?: error("Target Message is null in a Message Command! Bug?")

                GlobalScope.launch {
                    executor.execute(commandContext, targetMessage)
                    logger.debug { "Finished $applicationCommandType's execution!" }
                }
            }
        }
    }
}
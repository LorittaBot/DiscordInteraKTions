package net.perfectdreams.discordinteraktions.platforms.kord.utils

import dev.kord.common.entity.ApplicationCommandType
import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.common.commands.*
import net.perfectdreams.discordinteraktions.common.commands.options.SlashCommandArguments
import net.perfectdreams.discordinteraktions.common.requests.managers.RequestManager
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData
import net.perfectdreams.discordinteraktions.common.requests.RequestBridge
import net.perfectdreams.discordinteraktions.common.utils.InteraKTionsExceptions
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

        val interactionData = InteractionData(request.data.resolved.value?.toDiscordInteraKTionsResolvedObjects(guildId))

        when (applicationCommandType) {
            is ApplicationCommandType.Unknown -> {
                error("Received unknown command type! ID: ${applicationCommandType.value}")
            }
            ApplicationCommandType.ChatInput -> {
                logger.debug { "Subcommand Labels: $commandLabels; Root Options: $relativeOptions" }

                val command = CommandDeclarationUtils.getApplicationCommandDeclarationFromLabel<SlashCommandDeclaration>(commandManager, commandLabels)
                    ?: InteraKTionsExceptions.missingDeclaration("slash command")

                val executor = command.executor ?: InteraKTionsExceptions.missingExecutor("slash command")

                // Convert the Nested Options into a map, then we can access them with our Discord InteraKTion options!
                val arguments = CommandDeclarationUtils.convertOptions(
                    request,
                    executor,
                    relativeOptions ?: listOf()
                )

                GlobalScope.launch {
                    executor.execute(
                        createContext(
                            command,
                            bridge,
                            kordUser,
                            request,
                            interactionData,
                            guildId
                        ),
                        SlashCommandArguments(arguments)
                    )
                    logger.debug { "Finished $applicationCommandType's execution!" }
                }
            }

            ApplicationCommandType.User -> {
                val command = CommandDeclarationUtils.getApplicationCommandDeclarationFromLabel<UserCommandDeclaration>(commandManager, commandLabels)
                    ?: InteraKTionsExceptions.missingDeclaration("user command")

                val executor = command.executor

                val targetUserId = request.data.targetId.value
                val targetUser = interactionData.resolved?.users?.get(targetUserId) ?: error("Target User is null in a User Command! Bug?")
                val targetMember = interactionData.resolved.members?.get(targetUserId)

                GlobalScope.launch {
                    executor.execute(
                        createContext(
                            command,
                            bridge,
                            kordUser,
                            request,
                            interactionData,
                            guildId
                        ),
                        targetUser,
                        targetMember
                    )
                    logger.debug { "Finished $applicationCommandType's execution!" }
                }
            }

            ApplicationCommandType.Message -> {
                val command = CommandDeclarationUtils.getApplicationCommandDeclarationFromLabel<MessageCommandDeclaration>(commandManager, commandLabels)
                    ?: InteraKTionsExceptions.missingDeclaration("message command")

                val executor = command.executor

                val targetMessageId = request.data.targetId.value
                val targetMessage = interactionData.resolved?.messages?.get(targetMessageId) ?: error("Target Message is null in a Message Command! Bug?")

                GlobalScope.launch {
                    executor.execute(
                        createContext(
                            command,
                            bridge,
                            kordUser,
                            request,
                            interactionData,
                            guildId
                        ),
                        targetMessage
                    )
                    logger.debug { "Finished $applicationCommandType's execution!" }
                }
            }
        }
    }

    private fun createContext(
        declaration: ApplicationCommandDeclaration,
        bridge: RequestBridge,
        kordUser: KordUser,
        request: DiscordInteraction,
        interactionData: InteractionData,
        guildId: Snowflake?
    ): ApplicationCommandContext {
        // If the guild ID is not null, then it means that the interaction happened in a guild!
        return if (guildId != null) {
            val member = request.member.value!! // Should NEVER be null!
            val kordMember = KordInteractionMember(
                guildId,
                member,
                KordUser(member.user.value!!) // Also should NEVER be null!
            )

            GuildApplicationCommandContext(
                bridge,
                kordUser,
                request.channelId,
                interactionData,
                request,
                declaration,
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
                interactionData,
                request,
                declaration
            )
        }
    }
}
package net.perfectdreams.discordinteraktions.platforms.kord.utils

import dev.kord.common.entity.Choice
import dev.kord.common.entity.CommandArgument
import dev.kord.common.entity.DiscordInteraction
import dev.kord.rest.builder.interaction.IntChoiceBuilder
import dev.kord.rest.builder.interaction.NumberChoiceBuilder
import dev.kord.rest.builder.interaction.StringChoiceBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.common.autocomplete.AutocompleteContext
import net.perfectdreams.discordinteraktions.common.autocomplete.AutocompleteExecutor
import net.perfectdreams.discordinteraktions.common.autocomplete.FocusedCommandOption
import net.perfectdreams.discordinteraktions.common.autocomplete.GuildAutocompleteContext
import net.perfectdreams.discordinteraktions.common.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.common.commands.GuildApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandDeclaration
import net.perfectdreams.discordinteraktions.common.commands.options.ChoiceableCommandOption
import net.perfectdreams.discordinteraktions.common.requests.managers.RequestManager
import net.perfectdreams.discordinteraktions.common.utils.InteraKTionsExceptions
import net.perfectdreams.discordinteraktions.common.commands.options.IntegerCommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.NullableIntegerCommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.NullableNumberCommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.NullableStringCommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.NumberCommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.StringCommandOption
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData
import net.perfectdreams.discordinteraktions.platforms.kord.commands.CommandDeclarationUtils
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordInteractionMember
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordUser

/**
 * Checks, matches and executes commands, this is a class because we share code between the `gateway-kord` and `webserver-ktor-kord` modules
 */
class KordAutocompleteChecker(val commandManager: CommandManager) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    fun checkAndExecute(request: DiscordInteraction, requestManager: RequestManager) {
        val bridge = requestManager.bridge

        logger.debug { request.data.name }

        // Processing subcommands is kinda hard, but not impossible!
        val commandLabels = CommandDeclarationUtils.findAllSubcommandDeclarationNames(request)
        val relativeOptions = CommandDeclarationUtils.getNestedOptions(request.data.options.value)
            ?: error("Relative Options are null on the request, this shouldn't happen on a autocomplete request! Bug?")

        val kordUser = KordUser(request.member.value?.user?.value ?: request.user.value ?: error("oh no"))
        val guildId = request.guildId.value

        val interactionData = InteractionData(request.data.resolved.value?.toDiscordInteraKTionsResolvedObjects(guildId))

        // If the guild ID is not null, then it means that the interaction happened in a guild!
        val autocompleteContext = if (guildId != null) {
            val member = request.member.value!! // Should NEVER be null!
            val kordMember = KordInteractionMember(
                guildId,
                member,
                KordUser(member.user.value!!) // Also should NEVER be null!
            )

            GuildAutocompleteContext(
                kordUser,
                request.channelId,
                interactionData,
                relativeOptions.filterIsInstance<CommandArgument<*>>(),
                request,
                guildId,
                kordMember
            )
        } else {
            AutocompleteContext(
                KordUser(
                    request.member.value?.user?.value ?: request.user.value ?: error("oh no")
                ),
                request.channelId,
                interactionData,
                relativeOptions.filterIsInstance<CommandArgument<*>>(),
                request
            )
        }

        val command = CommandDeclarationUtils.getApplicationCommandDeclarationFromLabel<SlashCommandDeclaration>(commandManager, commandLabels)
            ?: InteraKTionsExceptions.missingDeclaration("slash command")

        val slashCommandExecutorDeclaration = command.executor ?: return

        val focusedDiscordOption = relativeOptions.filterIsInstance<CommandArgument.AutoCompleteArgument>()
            .firstOrNull() ?: error("There isn't any autocomplete option on the autocomplete request! Bug?")

        require(focusedDiscordOption.focused.discordBoolean) { "Autocomplete argument is not set to focused! Bug?" }

        val option = slashCommandExecutorDeclaration.options.arguments.firstOrNull {
            it.name == focusedDiscordOption.name
        } ?: error("I couldn't find a matching option for ${focusedDiscordOption.name}! Did you update the application command body externally?")

        require(option is ChoiceableCommandOption<*, *>) { "Command option is not choiceable, so it can't be autocompleted! Bug?" }

        val autocompleteDeclaration = option.autoCompleteExecutorDeclaration ?: error("Received autocomplete request for ${focusedDiscordOption.name}, but there isn't any autocomplete executor declaration set on the option! Did you update the application command body externally?")
        val autocompleteExecutor = commandManager.autocompleteExecutors
            .firstOrNull { it.signature() == autocompleteDeclaration.parent } ?: InteraKTionsExceptions.missingExecutor("autocomplete")

        GlobalScope.launch {
            val focusedCommandOption = FocusedCommandOption(
                focusedDiscordOption.name,
                focusedDiscordOption.value
            )

            when (option) {
                is StringCommandOption, is NullableStringCommandOption -> {
                    autocompleteExecutor as AutocompleteExecutor<String>
                    val autocompleteResult = autocompleteExecutor.onAutocomplete(autocompleteContext, focusedCommandOption)
                    bridge.manager.sendStringAutocomplete(
                        (StringChoiceBuilder("<auto-complete>", "")
                            .apply {
                                for ((name, value) in autocompleteResult) {
                                    choice(name, value)
                                }
                            }.choices ?: listOf()) as List<Choice<String>>
                    )
                }

                is IntegerCommandOption, is NullableIntegerCommandOption -> {
                    autocompleteExecutor as AutocompleteExecutor<Long>
                    val autocompleteResult = autocompleteExecutor.onAutocomplete(autocompleteContext, focusedCommandOption)
                    bridge.manager.sendIntegerAutocomplete(
                        (IntChoiceBuilder("<auto-complete>", "")
                            .apply {
                                for ((name, value) in autocompleteResult) {
                                    choice(name, value)
                                }
                            }.choices ?: listOf()) as List<Choice<Long>>
                    )
                }

                is NumberCommandOption, is NullableNumberCommandOption -> {
                    autocompleteExecutor as AutocompleteExecutor<Double>
                    val autocompleteResult = autocompleteExecutor.onAutocomplete(autocompleteContext, focusedCommandOption)
                    bridge.manager.sendNumberAutocomplete(
                        NumberChoiceBuilder("<auto-complete>", "")
                            .apply {
                                for ((name, value) in autocompleteResult) {
                                    choice(name, value)
                                }
                            }.choices as List<Choice<Double>>
                    )
                }

                else -> error("Unsupported Autocomplete type ${option::class}")
            }
        }
    }
}
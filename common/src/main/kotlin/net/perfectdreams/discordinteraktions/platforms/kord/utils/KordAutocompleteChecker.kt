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
import net.perfectdreams.discordinteraktions.common.autocomplete.AutocompleteExecutor
import net.perfectdreams.discordinteraktions.common.autocomplete.FocusedCommandOption
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandDeclaration
import net.perfectdreams.discordinteraktions.common.context.manager.RequestManager
import net.perfectdreams.discordinteraktions.common.utils.InteraKTionsExceptions
import net.perfectdreams.discordinteraktions.common.commands.options.CommandOptionType
import net.perfectdreams.discordinteraktions.platforms.kord.commands.CommandDeclarationUtils

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

        val command = CommandDeclarationUtils.getApplicationCommandDeclarationFromLabel<SlashCommandDeclaration>(commandManager, commandLabels)
            ?: InteraKTionsExceptions.missingDeclaration("slash command")

        val slashCommandExecutorDeclaration = command.executor ?: return

        val focusedDiscordOption = relativeOptions.filterIsInstance<CommandArgument.AutoCompleteArgument>()
            .firstOrNull() ?: error("There isn't any autocomplete option on the autocomplete request! Bug?")

        require(focusedDiscordOption.focused.discordBoolean) { "Autocomplete argument is not set to focused! Bug?" }

        val option = slashCommandExecutorDeclaration.options.arguments.firstOrNull {
            it.name == focusedDiscordOption.name
        } ?: error("I couldn't find a matching option for ${focusedDiscordOption.name}! Did you update the application command body externally?")

        val autocompleteDeclaration = option.autoCompleteExecutorDeclaration ?: error("Received autocomplete request for ${focusedDiscordOption.name}, but there isn't any autocomplete executor declaration set on the option! Did you update the application command body externally?")
        val autocompleteExecutor = commandManager.autocompleteExecutors
            .firstOrNull { it.signature() == autocompleteDeclaration.parent } ?: InteraKTionsExceptions.missingExecutor("autocomplete")

        GlobalScope.launch {
            val focusedCommandOption = FocusedCommandOption(
                focusedDiscordOption.name,
                focusedDiscordOption.value
            )

            when (option.type) {
                CommandOptionType.String, CommandOptionType.NullableString -> {
                    autocompleteExecutor as AutocompleteExecutor<String>
                    val autocompleteResult = autocompleteExecutor.onAutocomplete(focusedCommandOption)
                    bridge.manager.sendStringAutocomplete(
                        (StringChoiceBuilder("<auto-complete>", "")
                            .apply {
                                for ((name, value) in autocompleteResult) {
                                    choice(name, value)
                                }
                            }.choices ?: listOf()) as List<Choice<String>>
                    )
                }

                CommandOptionType.Integer, CommandOptionType.NullableInteger -> {
                    autocompleteExecutor as AutocompleteExecutor<Long>
                    val autocompleteResult = autocompleteExecutor.onAutocomplete(focusedCommandOption)
                    bridge.manager.sendIntegerAutocomplete(
                        (IntChoiceBuilder("<auto-complete>", "")
                            .apply {
                                for ((name, value) in autocompleteResult) {
                                    choice(name, value)
                                }
                            }.choices ?: listOf()) as List<Choice<Long>>
                    )
                }

                CommandOptionType.Number, CommandOptionType.NullableNumber -> {
                    autocompleteExecutor as AutocompleteExecutor<Double>
                    val autocompleteResult = autocompleteExecutor.onAutocomplete(focusedCommandOption)
                    bridge.manager.sendNumberAutocomplete(
                        NumberChoiceBuilder("<auto-complete>", "")
                            .apply {
                                for ((name, value) in autocompleteResult) {
                                    choice(name, value)
                                }
                            }.choices as List<Choice<Double>>
                    )
                }

                else -> error("Unsupported Autocomplete type ${option.type}")
            }
        }
    }
}
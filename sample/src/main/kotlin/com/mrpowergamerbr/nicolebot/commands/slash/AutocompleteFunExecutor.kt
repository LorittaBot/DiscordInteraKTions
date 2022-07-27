package com.mrpowergamerbr.nicolebot.commands

import net.perfectdreams.discordinteraktions.common.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.commands.options.ApplicationCommandOptions
import net.perfectdreams.discordinteraktions.common.commands.options.SlashCommandArguments

class AutocompleteFunExecutor : SlashCommandExecutor() {
    inner class Options : ApplicationCommandOptions() {
        val text = string("text", "Text")

        val autocompleteText = string("autocomplete_text", "Autocomplete Text") {
            autocomplete { context, focused ->
                val filledValue = focused.value

                // We can get the value that the user has already filled in other options
                val textOptionValue = context.getArgument(options.text)

                // Description -> Value
                // You can send 25 autocomplete fields
                return@autocomplete mapOf(
                    "You typed $filledValue" to filledValue,
                    "You typed $textOptionValue" to textOptionValue,
                    "You typed ${textOptionValue.reversed()}" to textOptionValue.reversed()
                )
            }
        }
    }

    override val options = Options()

    override suspend fun execute(
        context: ApplicationCommandContext,
        args: SlashCommandArguments
    ) {
        context.sendEphemeralMessage {
            content = "You typed ${args[options.text]} and ${args[options.autocompleteText]}"
        }
    }
}
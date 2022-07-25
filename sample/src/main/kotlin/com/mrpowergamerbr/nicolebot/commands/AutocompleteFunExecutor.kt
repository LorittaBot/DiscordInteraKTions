package com.mrpowergamerbr.nicolebot.commands

import net.perfectdreams.discordinteraktions.common.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.commands.options.ApplicationCommandOptions
import net.perfectdreams.discordinteraktions.common.commands.options.SlashCommandArguments

class AutocompleteFunExecutor : SlashCommandExecutor() {
    companion object : SlashCommandExecutorDeclaration() {
        object Options : ApplicationCommandOptions() {
            val text = string("text", "Text")

            val autocompleteText = string("autocomplete_text", "Autocomplete Text") {
                autocomplete(AutocompleteFunAutocompleteExecutor)
            }
        }

        override val options = Options
    }

    override suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments) {
        context.sendEphemeralMessage {
            content = "You typed ${args[options.text]} and ${args[options.autocompleteText]}"
        }
    }
}
package com.mrpowergamerbr.nicolebot.commands

import com.mrpowergamerbr.nicolebot.commands.SendYourAttachmentExecutor.Companion.Options.register
import com.mrpowergamerbr.nicolebot.commands.SendYourAttachmentExecutor.Companion.Options.string
import com.mrpowergamerbr.nicolebot.commands.declarations.AutocompleteFunAutocompleteExecutor
import net.perfectdreams.discordinteraktions.common.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.commands.options.ApplicationCommandOptions
import net.perfectdreams.discordinteraktions.common.commands.options.SlashCommandArguments

class AutocompleteFunExecutor : SlashCommandExecutor() {
    companion object : SlashCommandExecutorDeclaration(AutocompleteFunExecutor::class) {
        object Options : ApplicationCommandOptions() {
            val text = string("text", "Text")
                .register()

            val autocompleteText = string("autocomplete_text", "Autocomplete Text")
                .autocomplete(AutocompleteFunAutocompleteExecutor)
                .register()
        }

        override val options = Options
    }

    override suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments) {
        context.sendEphemeralMessage {
            content = "You typed ${args[options.text]} and ${args[options.autocompleteText]}"
        }
    }
}
package com.mrpowergamerbr.nicolebot.commands.slash

import com.mrpowergamerbr.nicolebot.utils.LanguageManager
import net.perfectdreams.discordinteraktions.common.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.commands.options.ApplicationCommandOptions
import net.perfectdreams.discordinteraktions.common.commands.options.SlashCommandArguments

class ExternallyProvidedStringExecutor(private val languageManager: LanguageManager) : SlashCommandExecutor() {
    inner class Options : ApplicationCommandOptions() {
        val example = string("example", languageManager.get("command_option")) {
            choice(languageManager.get("command_choice_name"), "fancy")
        }
    }

    override val options = Options()

    override suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments) {
        context.sendMessage {
            content = "Hello World! ${args[options.example]}"
        }
    }
}
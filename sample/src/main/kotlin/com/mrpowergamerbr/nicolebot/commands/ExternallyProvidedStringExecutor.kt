package com.mrpowergamerbr.nicolebot.commands

import com.mrpowergamerbr.nicolebot.utils.ExternalStringData
import net.perfectdreams.discordinteraktions.common.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.commands.options.ApplicationCommandOptions
import net.perfectdreams.discordinteraktions.common.commands.options.SlashCommandArguments

class ExternallyProvidedStringExecutor : SlashCommandExecutor() {
    companion object : SlashCommandExecutorDeclaration() {
        object Options : ApplicationCommandOptions() {
            val example = string("example", ExternalStringData("command_option")) {
                choice(ExternalStringData("command_choice_name"), "fancy")
            }
        }

        override val options = Options
    }

    override suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments) {
        context.sendMessage {
            content = "Hello World! ${args[Options.example]}"
        }
    }
}
package com.mrpowergamerbr.nicolebot.commands

import com.mrpowergamerbr.nicolebot.utils.customoptions.stringList
import net.perfectdreams.discordinteraktions.common.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.commands.options.ApplicationCommandOptions
import net.perfectdreams.discordinteraktions.common.commands.options.SlashCommandArguments

class CustomOptionsExecutor : SlashCommandExecutor() {
    inner class Options : ApplicationCommandOptions() {
        val choices = stringList("choice", "The list of options that you want me to choose for you", minimum = 2, maximum = 25)
    }

    override val options = Options()

    override suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments) {
        val choices = args[options.choices]

        context.sendMessage {
            content = buildString {
                append("Hmmm... I've chosen ${choices.random()}! :3")
            }
        }
    }
}
package com.mrpowergamerbr.nicolebot.commands

import com.mrpowergamerbr.nicolebot.utils.customoptions.delayedSuspendable
import net.perfectdreams.discordinteraktions.common.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.commands.options.ApplicationCommandOptions
import net.perfectdreams.discordinteraktions.common.commands.options.SlashCommandArguments

class DSCustomOptionsExecutor : SlashCommandExecutor() {
    companion object : SlashCommandExecutorDeclaration() {
        object Options : ApplicationCommandOptions() {
            val delayedSuspendable = delayedSuspendable("delayed_suspendable", "This option is delayed and suspendable")
        }

        override val options = Options
    }

    override suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments) {
        context.deferChannelMessage()

        // Yeah, it would've been nicer if it was more "seamless" instead of manually calling ".parse()" on a suspendable method
        // But there isn't a good way to handle this: Map getter operators can't be suspendable, so creating an extension method for
        // "args[Options.delayedSuspendable]" wouldn't have worked.
        // And trying to force everything on a single object also wouldn't have worked, what if you want to provide additional context when
        // parsing an option? (Example: i18n keys)
        val result = args[Options.delayedSuspendable].parse()

        context.sendMessage {
            content = buildString {
                append("Result: $result")
            }
        }
    }
}
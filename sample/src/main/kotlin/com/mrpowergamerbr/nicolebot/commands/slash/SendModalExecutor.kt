package com.mrpowergamerbr.nicolebot.commands.slash

import net.perfectdreams.discordinteraktions.common.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.commands.options.SlashCommandArguments
import net.perfectdreams.discordinteraktions.common.modals.components.textInput

class SendModalExecutor : SlashCommandExecutor() {
    override suspend fun execute(
        context: ApplicationCommandContext,
        args: SlashCommandArguments
    ) {
        context.sendModal(ModalYayExecutor, "Hello World!!") {
            actionRow {
                textInput(ModalYayExecutor.options.something, "something cool and epic!") {
                    placeholder = "Loritta is very cool and epic :3"
                }
            }

            actionRow {
                textInput(ModalYayExecutor.options.somethingEvenBigger, "How's your day?") {
                    value = "It is going fine, thank you :D"
                }
            }
        }
    }
}
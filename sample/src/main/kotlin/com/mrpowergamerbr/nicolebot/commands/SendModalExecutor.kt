package com.mrpowergamerbr.nicolebot.commands

import dev.kord.common.entity.TextInputStyle
import net.perfectdreams.discordinteraktions.common.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.commands.options.SlashCommandArguments
import net.perfectdreams.discordinteraktions.common.modals.components.textInput

class SendModalExecutor : SlashCommandExecutor() {
    companion object : SlashCommandExecutorDeclaration(SendModalExecutor::class)

    override suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments) {
        context.sendModal(ModalSubmitYayExecutor, "Hello World!!") {
            actionRow {
                textInput(ModalSubmitYayExecutor.options.something, TextInputStyle.Short, "something cool and epic!") {}
            }

            actionRow {
                textInput(ModalSubmitYayExecutor.options.somethingEvenBigger, TextInputStyle.Paragraph, "How's your day?") {
                    allowedLength = 50..200
                }
            }
        }
    }
}
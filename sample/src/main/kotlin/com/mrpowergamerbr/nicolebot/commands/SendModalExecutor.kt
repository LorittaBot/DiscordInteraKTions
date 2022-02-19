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
        // Due to the way modals work on Discord, everyone will see the same modal data if a user doesn't submit it.
        // To workaround this, you can provide a custom data on the "sendModal"
        // context.sendModal(ModalSubmitYayExecutor, context.sender.id.toString(), "Hello World!!")
        context.sendModal(ModalSubmitYayExecutor, context.sender.id.toString(), "Hello World!!") {
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
package com.mrpowergamerbr.nicolebot.commands

import dev.kord.common.entity.ButtonStyle
import net.perfectdreams.discordinteraktions.common.builder.message.actionRow
import net.perfectdreams.discordinteraktions.common.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.commands.options.SlashCommandArguments
import net.perfectdreams.discordinteraktions.common.components.interactiveButton

class ButtonsExecutor : SlashCommandExecutor() {
    companion object : SlashCommandExecutorDeclaration()

    override suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments) {
        context.sendMessage {
            content = "Click on a button!"

            actionRow {
                interactiveButton(
                    ButtonStyle.Primary,
                    FancyButtonClickExecutor,
                    "lori is so cute!! :3"
                ) {
                    label = "Fancy Button!"
                }
            }
        }
    }
}
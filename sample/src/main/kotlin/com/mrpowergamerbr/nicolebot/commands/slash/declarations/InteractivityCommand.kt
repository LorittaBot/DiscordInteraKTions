package com.mrpowergamerbr.nicolebot.commands.slash.declarations

import com.mrpowergamerbr.nicolebot.commands.slash.ButtonsExecutor
import com.mrpowergamerbr.nicolebot.commands.slash.SendModalExecutor
import dev.kord.common.Locale
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandDeclarationWrapper
import net.perfectdreams.discordinteraktions.common.commands.slashCommand

object InteractivityCommand : SlashCommandDeclarationWrapper {
    override fun declaration() = slashCommand("interactivity", "Showing off Discord InteraKTions' Interactivity") {
        subcommand("buttons", "Showing off Discord InteraKTions' Button Interactivity") {
            descriptionLocalizations = mapOf(
                Locale.PORTUGUESE_BRAZIL to "Mostrando como a interatividade de botões no Discord InteraKTions funciona"
            )

            executor = ButtonsExecutor()
        }

        subcommand("modal", "Showing off Discord InteraKTions' Modals Interactivity") {
            descriptionLocalizations = mapOf(
                Locale.PORTUGUESE_BRAZIL to "Mostrando como a interatividade de modals no Discord InteraKTions funciona"
            )

            executor = SendModalExecutor()
        }
    }
}
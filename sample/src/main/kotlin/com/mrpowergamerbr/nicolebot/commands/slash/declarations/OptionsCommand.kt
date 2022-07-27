package com.mrpowergamerbr.nicolebot.commands.declarations

import com.mrpowergamerbr.nicolebot.commands.*
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandDeclarationWrapper
import net.perfectdreams.discordinteraktions.common.commands.slashCommand

object OptionsCommand : SlashCommandDeclarationWrapper {
    override fun declaration() = slashCommand("options", "Shows all supported options") {
        subcommand("required", "All required options") {
            executor = RequiredOptionsExecutor()
        }

        subcommand("optional", "All optional options") {
            executor = OptionalOptionsExecutor()
        }

        subcommand("custom", "Showing how you can create and use custom options") {
            executor = CustomOptionsExecutor()
        }

        subcommand("dscustom", "Showing how you can create and use custom options") {
            executor = DSCustomOptionsExecutor()
        }
    }
}
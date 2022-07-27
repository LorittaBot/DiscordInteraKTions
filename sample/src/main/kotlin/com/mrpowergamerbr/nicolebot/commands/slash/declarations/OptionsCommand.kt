package com.mrpowergamerbr.nicolebot.commands.slash.declarations

import com.mrpowergamerbr.nicolebot.commands.slash.CustomOptionsExecutor
import com.mrpowergamerbr.nicolebot.commands.slash.DSCustomOptionsExecutor
import com.mrpowergamerbr.nicolebot.commands.slash.OptionalOptionsExecutor
import com.mrpowergamerbr.nicolebot.commands.slash.RequiredOptionsExecutor
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
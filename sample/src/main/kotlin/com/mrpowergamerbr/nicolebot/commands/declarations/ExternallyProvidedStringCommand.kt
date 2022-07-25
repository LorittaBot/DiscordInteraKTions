package com.mrpowergamerbr.nicolebot.commands.declarations

import com.mrpowergamerbr.nicolebot.commands.ExternallyProvidedStringExecutor
import com.mrpowergamerbr.nicolebot.utils.ExternalStringData
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandDeclarationWrapper
import net.perfectdreams.discordinteraktions.common.commands.slashCommand

object ExternallyProvidedStringCommand : SlashCommandDeclarationWrapper {
    override fun declaration() = slashCommand(ExternalStringData("command_label"), ExternalStringData("command_description")) {
        executor = ExternallyProvidedStringExecutor
    }
}
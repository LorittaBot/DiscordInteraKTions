package com.mrpowergamerbr.nicolebot.commands.slash.declarations

import com.mrpowergamerbr.nicolebot.commands.slash.AutocompleteFunExecutor
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandDeclarationWrapper
import net.perfectdreams.discordinteraktions.common.commands.slashCommand

object AutocompleteFunCommand : SlashCommandDeclarationWrapper {
    override fun declaration() = slashCommand("autocompletefun", "Fun with Autocomplete") {
        executor = AutocompleteFunExecutor()
    }
}
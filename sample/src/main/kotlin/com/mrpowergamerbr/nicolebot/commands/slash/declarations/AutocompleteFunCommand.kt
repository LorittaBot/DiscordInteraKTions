package com.mrpowergamerbr.nicolebot.commands.declarations

import com.mrpowergamerbr.nicolebot.commands.AutocompleteFunExecutor
import com.mrpowergamerbr.nicolebot.commands.HelloWorldExecutor
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandDeclarationWrapper
import net.perfectdreams.discordinteraktions.common.commands.slashCommand

object AutocompleteFunCommand : SlashCommandDeclarationWrapper {
    override fun declaration() = slashCommand("autocompletefun", "Fun with Autocomplete") {
        executor = AutocompleteFunExecutor()
    }
}
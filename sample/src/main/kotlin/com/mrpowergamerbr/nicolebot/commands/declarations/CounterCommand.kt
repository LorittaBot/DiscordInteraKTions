package com.mrpowergamerbr.nicolebot.commands.declarations

import com.mrpowergamerbr.nicolebot.commands.CounterExecutor
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandDeclarationWrapper
import net.perfectdreams.discordinteraktions.common.commands.slashCommand

object CounterCommand : SlashCommandDeclarationWrapper {
    override fun declaration() = slashCommand("counter", "Click to increase the counter!") {
        executor = CounterExecutor
    }
}
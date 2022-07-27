package com.mrpowergamerbr.nicolebot.commands.declarations

import com.mrpowergamerbr.nicolebot.commands.CounterExecutor
import com.mrpowergamerbr.nicolebot.utils.Counter
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandDeclarationWrapper
import net.perfectdreams.discordinteraktions.common.commands.slashCommand

class CounterCommand(val counter: Counter) : SlashCommandDeclarationWrapper {
    override fun declaration() = slashCommand("counter", "Click to increase the counter!") {
        executor = CounterExecutor(counter)
    }
}
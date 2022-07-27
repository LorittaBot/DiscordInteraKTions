package com.mrpowergamerbr.nicolebot.commands.slash.declarations

import com.mrpowergamerbr.nicolebot.commands.slash.HelloWorldExecutor
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandDeclarationWrapper
import net.perfectdreams.discordinteraktions.common.commands.slashCommand

object HelloWorldCommand : SlashCommandDeclarationWrapper {
    override fun declaration() = slashCommand("helloworld", "Hello, World!") {
        executor = HelloWorldExecutor()
    }
}
package com.mrpowergamerbr.nicolebot.commands.declarations

import com.mrpowergamerbr.nicolebot.commands.ExternallyProvidedStringExecutor
import com.mrpowergamerbr.nicolebot.utils.LanguageManager
import net.perfectdreams.discordinteraktions.common.attributes.AttributeKey
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandDeclarationWrapper
import net.perfectdreams.discordinteraktions.common.commands.slashCommand

class ExternallyProvidedStringCommand(private val languageManager: LanguageManager) : SlashCommandDeclarationWrapper {
    override fun declaration() = slashCommand(languageManager.get("command_label"), languageManager.get("command_description")) {
        executor = ExternallyProvidedStringExecutor(languageManager)
        // By default, Discord InteraKTions tries to initialize a class that has the name "Options", is nested within the executor, and has no constructor parameters.
        // You can explicitly set what Options you want to use, if your "Options" class requires any constructor parameters.
        //
        // However, this is annoying to do, that's why Discord InteraKTions can handle the object detection and initialization for us if you
        // provide your own options initialization handler!
        // Check out NicoleBot's "commandManager.addOptionsInitializationHandler" call :3
        // options = ExternallyProvidedStringExecutor.Options(languageManager)
    }
}
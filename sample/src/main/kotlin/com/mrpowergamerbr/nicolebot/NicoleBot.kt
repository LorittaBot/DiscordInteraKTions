package com.mrpowergamerbr.nicolebot

import com.mrpowergamerbr.nicolebot.commands.message.declarations.ContentLengthMessageCommand
import com.mrpowergamerbr.nicolebot.commands.slash.CounterButtonExecutor
import com.mrpowergamerbr.nicolebot.commands.slash.FancyButtonClickExecutor
import com.mrpowergamerbr.nicolebot.commands.slash.ModalYayExecutor
import com.mrpowergamerbr.nicolebot.commands.slash.declarations.*
import com.mrpowergamerbr.nicolebot.commands.user.declarations.ViewAvatarUserCommand
import com.mrpowergamerbr.nicolebot.utils.Counter
import com.mrpowergamerbr.nicolebot.utils.LanguageManager
import dev.kord.common.entity.Snowflake
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.platforms.kord.commands.KordCommandRegistry

// "Nicole" is an easter egg: Loritta's (https://loritta.website/) name was planned to be "Nicole" at one point
class NicoleBot(
    val rest: RestClient,
    val commandManager: CommandManager
) {
    companion object {
        val APPLICATION_ID = Snowflake(680539524400676977L)
        val GUILD_ID = Snowflake(936391274951503873L)
    }

    val counter = Counter(0)
    private val languageManager = LanguageManager()

    suspend fun registerCommands() {
        // ===[ /helloworld ]===
        commandManager.register(HelloWorldCommand)

        // ===[ /options ]===
        commandManager.register(OptionsCommand)

        // ===[ /interactivity ]===
        commandManager.register(InteractivityCommand)
        commandManager.register(FancyButtonClickExecutor())

        commandManager.register(
            ModalYayExecutor,
            ModalYayExecutor()
        )

        // ===[ /counter ]===
        commandManager.register(CounterCommand(counter))
        commandManager.register(CounterButtonExecutor(counter))

        // ===[ /sendyourattachment ]===
        commandManager.register(SendYourAttachmentCommand)

        // ===[ /autocompletefun ]===
        commandManager.register(AutocompleteFunCommand)

        // ===[ /externallyprovidedstring ]===
        commandManager.register(ExternallyProvidedStringCommand(languageManager))

        // ===[ "View avatar" ]===
        commandManager.register(ViewAvatarUserCommand)

        // ===[ "View content length" ]===
        commandManager.register(ContentLengthMessageCommand)

        val registry = KordCommandRegistry(
            APPLICATION_ID,
            rest,
            commandManager
        )

        registry.updateAllCommandsInGuild(GUILD_ID)
        // registry.updateAllGlobalCommands()
    }
}
package com.mrpowergamerbr.nicolebot

import com.mrpowergamerbr.nicolebot.commands.*
import com.mrpowergamerbr.nicolebot.commands.declarations.*
import com.mrpowergamerbr.nicolebot.utils.Counter
import com.mrpowergamerbr.nicolebot.utils.ExternalStringData
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
    val languageManager = LanguageManager()

    suspend fun registerCommands() {
        // With "addHandler", you can add a handler to handle externally provided strings for your command label, description, options, or whenever else
        // This is useful if you are pulling strings from a file (example: language system) and you don't want to hardcode the command structure strings
        // on your code!
        commandManager.handlers.addHandler {
            if (it !is ExternalStringData)
                return@addHandler null

            it.provide(languageManager)
        }

        // ===[ /helloworld ]===
        commandManager.register(
            HelloWorldCommand,
            HelloWorldExecutor()
        )

        // ===[ /options ]===
        commandManager.register(
            OptionsCommand,
            RequiredOptionsExecutor(),
            OptionalOptionsExecutor(),
            CustomOptionsExecutor(),
            DSCustomOptionsExecutor()
        )

        // ===[ /interactivity ]===
        commandManager.register(
            InteractivityCommand,
            ButtonsExecutor(),
            SendModalExecutor()
        )

        commandManager.register(
            FancyButtonClickExecutor,
            FancyButtonClickExecutor()
        )

        commandManager.register(
            ModalSubmitYayExecutor,
            ModalSubmitYayExecutor()
        )

        // ===[ /counter ]===
        commandManager.register(
            CounterCommand,
            CounterExecutor(counter)
        )

        commandManager.register(
            CounterButtonClickExecutor,
            CounterButtonClickExecutor(counter)
        )

        // ===[ /sendyourattachment ]===
        commandManager.register(
            SendYourAttachmentCommand,
            SendYourAttachmentExecutor()
        )

        // ===[ /autocompletefun ]===
        commandManager.register(
            AutocompleteFunCommand,
            AutocompleteFunExecutor()
        )

        commandManager.register(
            AutocompleteFunAutocompleteExecutor,
            AutocompleteFunAutocompleteExecutor()
        )

        // ===[ /externallyprovidedstring ]===
        commandManager.register(
            ExternallyProvidedStringCommand,
            ExternallyProvidedStringExecutor()
        )

        val registry = KordCommandRegistry(
            APPLICATION_ID,
            rest,
            commandManager
        )

        registry.updateAllCommandsInGuild(GUILD_ID)
        // registry.updateAllGlobalCommands()
    }
}
package com.mrpowergamerbr.nicolebot

import com.mrpowergamerbr.nicolebot.commands.AutocompleteFunExecutor
import com.mrpowergamerbr.nicolebot.commands.ButtonsExecutor
import com.mrpowergamerbr.nicolebot.commands.CounterButtonClickExecutor
import com.mrpowergamerbr.nicolebot.commands.CounterExecutor
import com.mrpowergamerbr.nicolebot.commands.FancyButtonClickExecutor
import com.mrpowergamerbr.nicolebot.commands.SendModalExecutor
import com.mrpowergamerbr.nicolebot.commands.HelloWorldExecutor
import com.mrpowergamerbr.nicolebot.commands.ModalSubmitYayExecutor
import com.mrpowergamerbr.nicolebot.commands.SendYourAttachmentExecutor
import com.mrpowergamerbr.nicolebot.commands.AutocompleteFunAutocompleteExecutor
import com.mrpowergamerbr.nicolebot.commands.declarations.AutocompleteFunCommand
import com.mrpowergamerbr.nicolebot.commands.declarations.CounterCommand
import com.mrpowergamerbr.nicolebot.commands.declarations.HelloWorldCommand
import com.mrpowergamerbr.nicolebot.commands.declarations.InteractivityCommand
import com.mrpowergamerbr.nicolebot.commands.declarations.SendYourAttachmentCommand
import com.mrpowergamerbr.nicolebot.utils.Counter
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
        val APPLICATION_ID = Snowflake(744361365724069898L)
        val GUILD_ID = Snowflake(297732013006389252L)
    }

    val counter = Counter(0)

    suspend fun registerCommands() {
        // ===[ /helloworld ]===
        commandManager.register(
            HelloWorldCommand,
            HelloWorldExecutor()
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

        val registry = KordCommandRegistry(
            APPLICATION_ID,
            rest,
            commandManager
        )

        registry.updateAllCommandsInGuild(GUILD_ID)
        // registry.updateAllGlobalCommands()
    }
}
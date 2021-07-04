package net.perfectdreams.discordinteraktions.webserver

import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.api.entities.Snowflake
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.context.commands.SlashCommandArguments
import net.perfectdreams.discordinteraktions.common.context.commands.SlashCommandContext
import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandExecutorDeclaration
import net.perfectdreams.discordinteraktions.declarations.slash.options.CommandOptions
import net.perfectdreams.discordinteraktions.declarations.slash.slashCommand
import net.perfectdreams.discordinteraktions.platforms.kord.commands.KordCommandRegistry
import java.io.File

suspend fun main() {
    val interactionsServer = InteractionsServer(
        744361365724069898L,
        File("public-key.txt").readText(),
        File("token.txt").readText(),
        12212
    )

    interactionsServer.commandManager.register(TestCommand, TestCommandExecutor())

    val registry = KordCommandRegistry(
        Snowflake(744361365724069898L),
        interactionsServer.rest,
        interactionsServer.commandManager
    )

    registry.updateAllCommandsInGuild(Snowflake(297732013006389252L), false)

    interactionsServer.start()
}

object TestCommand : SlashCommandDeclaration {
    override fun declaration() = slashCommand("test", "test owo") {
        subcommand("test", "test cmd") {
            executor = TestCommandExecutor
        }
    }
}

class TestCommandExecutor : SlashCommandExecutor() {
    companion object : SlashCommandExecutorDeclaration(TestCommandExecutor::class) {
        object Options : CommandOptions() {
            val integer = integer("test", "an integer idk")
                .choice(1, "haha, one!")
                .choice(2, "owo")
                .register()

            val ayaya = optionalBoolean("ayaya", "ayaya?")
                .register()
        }

        override val options = Options
    }

    override suspend fun execute(context: SlashCommandContext, args: SlashCommandArguments) {
        context.sendMessage {
            content = "The number is ${args[Options.integer]}, woaaa"
        }

        if (args[Options.ayaya] == true) {
            context.sendMessage {
                content = "ayaya!!!"
            }
        }
    }
}
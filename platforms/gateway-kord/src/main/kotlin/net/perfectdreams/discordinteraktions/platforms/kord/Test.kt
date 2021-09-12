package net.perfectdreams.discordinteraktions.platforms.kord

import dev.kord.common.entity.Snowflake
import dev.kord.gateway.DefaultGateway
import dev.kord.gateway.start
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.common.commands.slash.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.context.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.context.commands.slash.SlashCommandArguments
import net.perfectdreams.discordinteraktions.declarations.commands.slash.SlashCommandExecutorDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.slash.options.CommandOptions
import net.perfectdreams.discordinteraktions.declarations.commands.slash.slashCommand
import net.perfectdreams.discordinteraktions.declarations.commands.wrappers.SlashCommandDeclarationWrapper
import net.perfectdreams.discordinteraktions.platforms.kord.commands.KordCommandRegistry
import java.io.File

suspend fun main() {
    val rest = RestClient(File("token.txt").readText())
    val commandManager = CommandManager()

    commandManager.register(TestCommand, TestCommandExecutor())

    val registry = KordCommandRegistry(
        Snowflake(744361365724069898L),
        rest,
        commandManager
    )

    registry.updateAllCommandsInGuild(Snowflake(297732013006389252L), false)
    val gateway = DefaultGateway {}

    gateway.installDiscordInteraKTions(
        Snowflake(744361365724069898L),
        rest,
        commandManager
    )

    gateway.start(File("token.txt").readText())
}

object TestCommand : SlashCommandDeclarationWrapper {
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

    override suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments) {
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
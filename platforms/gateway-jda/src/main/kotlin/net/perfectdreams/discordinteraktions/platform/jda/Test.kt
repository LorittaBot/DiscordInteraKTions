package net.perfectdreams.discordinteraktions.platform.jda

import net.dv8tion.jda.api.JDABuilder
import net.perfectdreams.discordinteraktions.api.entities.Snowflake
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.context.SlashCommandArguments
import net.perfectdreams.discordinteraktions.common.context.SlashCommandContext
import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandExecutorDeclaration
import net.perfectdreams.discordinteraktions.declarations.slash.options.CommandOptions
import net.perfectdreams.discordinteraktions.declarations.slash.slashCommand
import net.perfectdreams.discordinteraktions.platform.jda.commands.JDACommandRegistry
import net.perfectdreams.discordinteraktions.platform.jda.listeners.SlashCommandListener
import java.io.File

suspend fun main() {
    val manager = CommandManager()
    manager.register(TestCommand, TestCommandExecutor(), SubcommandTestCommandExecutor())

    val jda = JDABuilder.createDefault(File("token.txt").readText())
        .addEventListeners(SlashCommandListener(manager))
        .build()
        .awaitReady()

    val jdaCommandRegistry = JDACommandRegistry(jda, manager)
    jdaCommandRegistry.updateAllCommandsInGuild(Snowflake(297732013006389252L), true)
}

object TestCommand : SlashCommandDeclaration {
    override fun declaration() = slashCommand("test", "test owo") {
        subcommand("test", "test cmd") {
            executor = TestCommandExecutor
        }

        subcommand("ayaya", "ayaya!!") {
            executor = SubcommandTestCommandExecutor
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
            content = "The number is ${args[options.integer]}, woaaa"
        }

        if (args[options.ayaya] == true) {
            context.sendMessage {
                content = "ayaya!!!"
            }
        }
    }
}

class SubcommandTestCommandExecutor : SlashCommandExecutor() {
    companion object : SlashCommandExecutorDeclaration(SubcommandTestCommandExecutor::class) {
        object Options : CommandOptions() {
            val ayaya = boolean("ayaya", "more ayaya?")
                .register()
        }

        override val options = Options
    }

    override suspend fun execute(context: SlashCommandContext, args: SlashCommandArguments) {
        context.defer(true)

        if (args[options.ayaya]) {
            context.sendMessage {
                content = "ayaya!!! <a:chino_AYAYA:696984642594537503>"
            }
        } else {
            context.sendMessage {
                content = "no ayaya but i will do it anyway!!! <a:chino_AYAYA:696984642594537503>"
            }

            context.sendMessage {
                content = "ayaya!!! <a:chino_AYAYA:696984642594537503>"

                addFile("ayaya.gif", File("L:\\Pictures\\loritta_fofis.gif").inputStream())
            }
        }
    }
}
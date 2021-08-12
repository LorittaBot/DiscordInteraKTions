package net.perfectdreams.discordinteraktions.webserver

import dev.kord.common.Color
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.api.entities.Snowflake
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.context.commands.SlashCommandArguments
import net.perfectdreams.discordinteraktions.common.context.commands.SlashCommandContext
import net.perfectdreams.discordinteraktions.common.utils.AllowedMentions
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

    interactionsServer.commandManager.register(TestCommand, TestCommandExecutor(), TestCommand2Executor(), TestEmbedExecutor())

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
        subcommandGroup("ayaya", "test group") {
            subcommand("test", "test cmd") {
                executor = TestCommandExecutor
            }

            subcommand("test2", "test cmd2") {
                executor = TestCommand2Executor
            }

            subcommand("testembed", "tests an embed") {
                executor = TestEmbedExecutor
            }
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

            val user = optionalUser("user", "The user, maybe, idk")
                .register()
        }

        override val options = Options
    }

    override suspend fun execute(context: SlashCommandContext, args: SlashCommandArguments) {
        context.sendMessage {
            content = "The number is ${args[Options.integer]}, woaaa"

            allowedMentions = AllowedMentions(
                listOf(),
                listOf(),
                true
            )
        }

        if (args[Options.ayaya] == true) {
            context.sendMessage {
                content = "ayaya!!!"
            }
        }

        val user = args[options.user]
        if (user != null) {
            context.sendMessage {
                content = "User: $user"
            }
        }
    }
}

class TestCommand2Executor : SlashCommandExecutor() {
    companion object : SlashCommandExecutorDeclaration(TestCommand2Executor::class) {
        object Options : CommandOptions() {
            val test = string("test", "an string idk")
                .register()
        }

        override val options = Options
    }

    override suspend fun execute(context: SlashCommandContext, args: SlashCommandArguments) {
        context.sendMessage {
            content = "Text: ${args[Options.test]}"

            allowedMentions = AllowedMentions(
                listOf(),
                listOf(),
                true
            )
        }
    }
}

class TestEmbedExecutor : SlashCommandExecutor() {
    companion object : SlashCommandExecutorDeclaration(TestEmbedExecutor::class)

    override suspend fun execute(context: SlashCommandContext, args: SlashCommandArguments) {
        context.sendMessage {
            embed {
                title = "Hello world!"
                description = "ayaya"

                color(0, 181, 255)
                author("Loritta", "https://loritta.website/", "https://cdn.discordapp.com/emojis/585938576907305004.png?v=1")
                image("https://cdn.discordapp.com/emojis/585938576907305004.png?v=1")
                thumbnail("https://cdn.discordapp.com/emojis/585938576907305004.png?v=1")

                inlineField("ayaya", "uwu")
                inlineField("owo!!!", "yay")

                footer("Google", "https://google.com")
            }
        }
    }
}
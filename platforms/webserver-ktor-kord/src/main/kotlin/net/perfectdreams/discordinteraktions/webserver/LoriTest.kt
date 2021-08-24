package net.perfectdreams.discordinteraktions.webserver

import kotlinx.coroutines.delay
import net.perfectdreams.discordinteraktions.api.entities.Member
import net.perfectdreams.discordinteraktions.api.entities.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.commands.message.MessageCommandExecutor
import net.perfectdreams.discordinteraktions.common.commands.slash.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.commands.user.UserCommandExecutor
import net.perfectdreams.discordinteraktions.common.components.buttons.ButtonStyle
import net.perfectdreams.discordinteraktions.common.context.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.context.commands.slash.SlashCommandArguments
import net.perfectdreams.discordinteraktions.common.entities.messages.Message
import net.perfectdreams.discordinteraktions.common.utils.AllowedMentions
import net.perfectdreams.discordinteraktions.declarations.commands.message.MessageCommandExecutorDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.message.messageCommand
import net.perfectdreams.discordinteraktions.declarations.commands.slash.SlashCommandExecutorDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.slash.options.CommandOptions
import net.perfectdreams.discordinteraktions.declarations.commands.slash.slashCommand
import net.perfectdreams.discordinteraktions.declarations.commands.user.UserCommandExecutorDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.user.userCommand
import net.perfectdreams.discordinteraktions.declarations.commands.wrappers.MessageCommandDeclarationWrapper
import net.perfectdreams.discordinteraktions.declarations.commands.wrappers.SlashCommandDeclarationWrapper
import net.perfectdreams.discordinteraktions.declarations.commands.wrappers.UserCommandDeclarationWrapper
import net.perfectdreams.discordinteraktions.platforms.kord.commands.KordCommandRegistry
import java.io.File
import kotlin.random.Random

suspend fun main() {
    val interactionsServer = InteractionsServer(
        744361365724069898L,
        File("public-key.txt").readText(),
        File("token.txt").readText(),
        12212
    )

    interactionsServer.commandManager.register(
        TestCommand,
        TestCommandExecutor(),
        TestCommand2Executor(),
        TestEmbedExecutor()
    )

    interactionsServer.commandManager.register(
        TestUserCommand,
        TestUserExecutor()
    )

    interactionsServer.commandManager.register(
        TestMessageCommand,
        TestMessageExecutor()
    )

    interactionsServer.commandManager.register(
        TestClickExecutor,
        TestClickExecutor()
    )

    interactionsServer.commandManager.register(
        TestClickWithDataExecutor,
        TestClickWithDataExecutor()
    )

    val registry = KordCommandRegistry(
        Snowflake(744361365724069898L),
        interactionsServer.rest,
        interactionsServer.commandManager
    )

    // registry.updateAllCommandsInGuild(Snowflake(297732013006389252L), false)

    interactionsServer.start()
}

object TestUserCommand : UserCommandDeclarationWrapper {
    override fun declaration() = userCommand("View User's Avatar", TestUserExecutor)
}

class TestUserExecutor : UserCommandExecutor() {
    companion object : UserCommandExecutorDeclaration(TestUserExecutor::class)

    override suspend fun execute(context: ApplicationCommandContext, targetUser: User, targetMember: Member?) {
        context.sendEphemeralMessage {
            content = "owo whats this??? ${targetUser.avatar.url}"
        }

        if (targetMember != null) {
            context.sendEphemeralMessage {
                content = "E você sabia que esse dai tem os cargos ${targetMember.roles}???"
            }
        }
    }
}

object TestMessageCommand : MessageCommandDeclarationWrapper {
    override fun declaration() = messageCommand("Furrify", TestMessageExecutor)
}

class TestMessageExecutor : MessageCommandExecutor() {
    companion object : MessageCommandExecutorDeclaration(TestMessageExecutor::class)

    override suspend fun execute(context: ApplicationCommandContext, targetMessage: Message) {
        context.sendMessage {
            content = furrify(targetMessage.content)
        }
    }

    val replacements = mapOf(
        "tal" to "taw",
        "quer" to "quew",
        "ser" to "sew",
        "dir" to "diw",
        "per" to "pew",
        "par" to "paw",
        "eat" to "eaw",
        "vez" to "vew",
        "isso" to "issu",
        "dio" to "diu",
        "bado" to "bad",
        "dos" to "dus",
        "mente" to "ment",
        "servidor" to "servidOwOr",
        "Loritta" to "OwOrittaw",
        "R" to "W",
        "L" to "W",
        "ow" to "OwO",
        "no" to "nu",
        "has" to "haz",
        "have" to "haz",
        "you" to "uu",
        "the " to "da ",
        "fofo" to "foof",
        "fofa" to "foof",
        "ito" to "it",
        "dade" to "dad",
        "tando" to "tand",
        "ens" to "e",
        "tas" to "ts",
        "quanto" to "quant",
        "ente" to "ent",
        "não" to "naum"
    )

    val suffixes = listOf(
        ":3",
        "UwU",
        "ʕʘ‿ʘʔ",
        ">_>",
        "^_^",
        "^-^",
        ";_;",
        ";-;",
        "xD",
        "x3",
        ":D",
        ":P",
        ";3",
        "XDDD",
        "ㅇㅅㅇ",
        "(人◕ω◕)",
        "（＾ｖ＾）",
        ">_<"
    )

    fun furrify(input: String): String {
        var new = input

        val suffix = when {
            new.contains("triste", true) || new.contains("desculp", true) || new.contains("sorry", true) -> ">_<"
            new.contains("parabéns", true) -> "(人◕ω◕)"
            else -> suffixes.random(Random(new.hashCode()))
        }

        for ((from, to) in replacements) {
            new = new.replace(from, to)
        }

        new += " $suffix"
        new = new.trim()

        return new
    }
}

object TestCommand : SlashCommandDeclarationWrapper {
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

    override suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments) {
        context.sendMessage {
            content = "hello world!"

            actionRow {
                interactiveButton(
                    ButtonStyle.Primary,
                    "ayaya",
                    TestClickExecutor
                )
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

    override suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments) {
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

    override suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments) {
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
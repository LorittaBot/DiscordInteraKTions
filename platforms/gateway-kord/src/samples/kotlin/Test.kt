import dev.kord.common.entity.Snowflake
import dev.kord.gateway.DefaultGateway
import dev.kord.gateway.start
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.autocomplete.AutocompleteExecutor
import net.perfectdreams.discordinteraktions.common.autocomplete.AutocompleteExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.autocomplete.CommandOption
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.common.commands.slash.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.context.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.context.commands.slash.SlashCommandArguments
import net.perfectdreams.discordinteraktions.declarations.commands.slash.SlashCommandExecutorDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.slash.options.CommandOptions
import net.perfectdreams.discordinteraktions.declarations.commands.slash.slashCommand
import net.perfectdreams.discordinteraktions.declarations.commands.wrappers.SlashCommandDeclarationWrapper
import net.perfectdreams.discordinteraktions.platforms.kord.commands.KordCommandRegistry
import net.perfectdreams.discordinteraktions.platforms.kord.installDiscordInteraKTions
import java.io.File
import java.util.*

suspend fun main() {
    val rest = RestClient(File("token.txt").readText())
    val commandManager = CommandManager()

    commandManager.register(
        TestCommand,
        TestCommandExecutor(),
        TestACCommandExecutor()
    )

    commandManager.register(
        AutocompleteTestExecutor,
        AutocompleteTestExecutor()
    )

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

        subcommand("ac", "test autocomplete") {
            executor = TestACCommandExecutor
        }
    }
}

class AutocompleteTestExecutor : AutocompleteExecutor<String> {
    companion object : AutocompleteExecutorDeclaration<String>(AutocompleteTestExecutor::class)

    override suspend fun onAutocomplete(focusedOption: CommandOption<String>): Map<String, String> {
        // TODO: Maybe create a nice DSL similar to Kord?
        return mapOf(
            "What you typed: ${focusedOption.value}" to focusedOption.value,
            "OwO!" to "owo",
            "UwU!" to "uwu",
            UUID.randomUUID().toString() to "ayaya"
        )
    }
}

class TestACCommandExecutor : SlashCommandExecutor() {
    companion object : SlashCommandExecutorDeclaration(TestACCommandExecutor::class) {
        object Options : CommandOptions() {
            val str = string("test", "an integer idk")
                .autocomplete(AutocompleteTestExecutor)
                .register()
        }

        override val options = Options
    }

    override suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments) {
        context.sendMessage {
            content = args[Options.str]
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
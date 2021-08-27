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
        TestCommandExecutor()
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

object TestCommand : SlashCommandDeclarationWrapper {
    override fun declaration() = slashCommand("test", "test owo") {
        executor = TestCommandExecutor
    }
}

class TestCommandExecutor : SlashCommandExecutor() {
    companion object : SlashCommandExecutorDeclaration(TestCommandExecutor::class)

    override suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments) {
        // Defers the message
        context.deferChannelMessage()

        context.sendMessage {
            content = "owo"

            file("gessy_pistola.png", File("L:\\Pictures\\gessy_pistola.png").inputStream())

            components {
                actionRow {
                    interactiveButton(
                        ButtonStyle.Primary,
                        "Clique!",
                        TestClickExecutor
                    )
                }
            }
        }
    }
}
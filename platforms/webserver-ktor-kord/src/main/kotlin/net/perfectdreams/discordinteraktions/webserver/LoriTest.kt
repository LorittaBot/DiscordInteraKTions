package net.perfectdreams.discordinteraktions.webserver

import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.DiscordPartialEmoji
import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.message.create.actionRow
import net.perfectdreams.discordinteraktions.common.commands.slash.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.components.interactiveButton
import net.perfectdreams.discordinteraktions.common.components.selectMenu
import net.perfectdreams.discordinteraktions.common.components.selects.SelectMenuExecutor
import net.perfectdreams.discordinteraktions.common.context.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.context.commands.slash.SlashCommandArguments
import net.perfectdreams.discordinteraktions.declarations.commands.slash.SlashCommandExecutorDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.slash.slashCommand
import net.perfectdreams.discordinteraktions.declarations.commands.wrappers.SlashCommandDeclarationWrapper
import net.perfectdreams.discordinteraktions.platforms.kord.commands.KordCommandRegistry
import java.io.File

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

    interactionsServer.commandManager.register(
        SelectExecutor,
        SelectExecutor()
    )

    val registry = KordCommandRegistry(
        Snowflake(744361365724069898L),
        interactionsServer.rest,
        interactionsServer.commandManager
    )

    registry.updateAllCommandsInGuild(Snowflake(297732013006389252L), false)

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
        // context.deferChannelMessage()
        val msg = context.sendMessage {
            content = "owo"

            actionRow {
                /* interactiveButton(
                    ButtonStyle.Primary,
                    "Clique se quiser talvez nn sei",
                    TestClickExecutor
                ) */

                selectMenu(SelectExecutor) {
                    this.allowedValues = 0..3
                    this.placeholder = "clique aí e selecione algo supimpa"

                    option("isso ainda não faz nada", "nada1")
                    option("mas é bem interessante vamos ser sinceros", "nada2")
                    option("carai olha o gessy", "gessy") {
                        this.emoji = DiscordPartialEmoji(
                            Snowflake(593907632784408644),
                            "smol_gessy"
                        )
                    }
                }
            }
        }
    }
}
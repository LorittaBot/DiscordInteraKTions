package net.perfectdreams.discordinteraktions.platform.jda

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.dv8tion.jda.api.JDABuilder
import net.perfectdreams.discordinteraktions.api.entities.Snowflake
import net.perfectdreams.discordinteraktions.common.buttons.ButtonStateManager
import net.perfectdreams.discordinteraktions.common.buttons.MemoryButtonStateManager
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.context.commands.SlashCommandArguments
import net.perfectdreams.discordinteraktions.common.context.commands.SlashCommandContext
import net.perfectdreams.discordinteraktions.common.utils.ActionRowComponent
import net.perfectdreams.discordinteraktions.common.utils.ButtonStyle
import net.perfectdreams.discordinteraktions.common.utils.TestData
import net.perfectdreams.discordinteraktions.common.utils.button
import net.perfectdreams.discordinteraktions.common.utils.urlButton
import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandExecutorDeclaration
import net.perfectdreams.discordinteraktions.declarations.slash.options.CommandOptions
import net.perfectdreams.discordinteraktions.declarations.slash.slashCommand
import net.perfectdreams.discordinteraktions.platform.jda.commands.JDACommandRegistry
import net.perfectdreams.discordinteraktions.platform.jda.listeners.SlashCommandListener
import net.perfectdreams.discordinteraktions.platform.jda.utils.ButtonClickExecutorTest
import java.io.File

suspend fun main() {
    val manager = CommandManager()
    val buttonsStateManager = MemoryButtonStateManager()

    manager.register(TestCommand, TestCommandExecutor(), SubcommandTestCommandExecutor(buttonsStateManager))
    buttonsStateManager.registerButtonExecutor(ButtonClickExecutorTest())

    val jda = JDABuilder.createDefault(File("token.txt").readText())
        .setRawEventsEnabled(true)
        .addEventListeners(SlashCommandListener(manager, buttonsStateManager))
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

class SubcommandTestCommandExecutor(val stateManager: ButtonStateManager) : SlashCommandExecutor() {
    companion object : SlashCommandExecutorDeclaration(SubcommandTestCommandExecutor::class) {
        object Options : CommandOptions() {
            val ayaya = boolean("ayaya", "more ayaya?")
                .register()
        }

        override val options = Options
    }

    override suspend fun execute(context: SlashCommandContext, args: SlashCommandArguments) {
        context.deferReply(false)

        if (args[options.ayaya]) {
            context.sendMessage {
                content = "ayaya!!! <a:chino_AYAYA:696984642594537503>"
            }
        } else {
            val stateHowdy = stateManager.storeState(
                ButtonClickExecutorTest::class,
                Json.encodeToString(TestData("howdy!"))
            )

            val stateUwu = stateManager.storeState(
                ButtonClickExecutorTest::class,
                Json.encodeToString(TestData("uwu whats this"))
            )

            context.sendMessage {
                content = "no ayaya but i will do it anyway!!! <a:chino_AYAYA:696984642594537503>"

                components.add(
                    ActionRowComponent(
                        listOf(
                            button(
                                ButtonStyle.PRIMARY,
                                "ayaya!!",
                                stateUniqueId = stateHowdy
                            ),
                            button(
                                ButtonStyle.SECONDARY,
                                "owo!!",
                                stateUniqueId = stateUwu
                            ),
                            urlButton("test", "https://youtu.be/a73vEHy4rZs")
                        )
                    )
                )
            }

            context.sendMessage {
                content = "ayaya!!! <a:chino_AYAYA:696984642594537503>"

                addFile("ayaya.gif", File("L:\\Pictures\\loritta_fofis.gif").inputStream())
            }
        }
    }
}
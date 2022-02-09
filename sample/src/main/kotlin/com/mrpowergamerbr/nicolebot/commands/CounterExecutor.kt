package com.mrpowergamerbr.nicolebot.commands

import com.mrpowergamerbr.nicolebot.utils.Counter
import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.message.create.UserMessageCreateBuilder
import dev.kord.rest.service.RestClient
import kotlinx.serialization.json.JsonNull.content
import net.perfectdreams.discordinteraktions.common.builder.message.MessageBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.actionRow
import net.perfectdreams.discordinteraktions.common.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.commands.options.SlashCommandArguments
import net.perfectdreams.discordinteraktions.common.components.interactiveButton
import net.perfectdreams.discordinteraktions.common.entities.messages.editMessage

class CounterExecutor(private val counter: Counter) : SlashCommandExecutor() {
    companion object : SlashCommandExecutorDeclaration(CounterExecutor::class) {

        fun createCounterMessage(currentCount: Int): MessageBuilder.() -> (Unit) = {
            content = """Current count: $currentCount
                |
                |Click the button to increase the counter!
            """.trimMargin()

            actionRow {
                interactiveButton(
                    ButtonStyle.Primary,
                    CounterButtonClickExecutor
                ) {
                    label = "Increase the counter!"
                }
            }
        }
    }

    override suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments) {
        val currentCount = counter.get()

        context.sendMessage {
            apply(createCounterMessage(currentCount))
        }
    }
}
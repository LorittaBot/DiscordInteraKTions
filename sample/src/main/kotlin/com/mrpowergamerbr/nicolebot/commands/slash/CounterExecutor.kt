package com.mrpowergamerbr.nicolebot.commands.slash

import com.mrpowergamerbr.nicolebot.utils.Counter
import dev.kord.common.entity.ButtonStyle
import net.perfectdreams.discordinteraktions.common.builder.message.MessageBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.actionRow
import net.perfectdreams.discordinteraktions.common.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.commands.options.SlashCommandArguments
import net.perfectdreams.discordinteraktions.common.components.interactiveButton

class CounterExecutor(private val counter: Counter) : SlashCommandExecutor() {
    companion object {
        fun createCounterMessage(currentCount: Int): MessageBuilder.() -> (Unit) = {
            content = """Current count: $currentCount
                |
                |Click the button to increase the counter!
            """.trimMargin()

            actionRow {
                interactiveButton(
                    ButtonStyle.Primary,
                    CounterButtonExecutor
                ) {
                    label = "Increase the counter!"
                }
            }
        }
    }

    override suspend fun execute(
        context: ApplicationCommandContext,
        args: SlashCommandArguments
    ) {
        val currentCount = counter.get()

        context.sendMessage {
            apply(createCounterMessage(currentCount))
        }
    }
}
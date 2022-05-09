package com.mrpowergamerbr.nicolebot.commands

import com.mrpowergamerbr.nicolebot.utils.Counter
import net.perfectdreams.discordinteraktions.common.components.ButtonClickExecutor
import net.perfectdreams.discordinteraktions.common.components.ButtonClickExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.components.ComponentContext
import net.perfectdreams.discordinteraktions.common.entities.User

class CounterButtonClickExecutor(private val counter: Counter) : ButtonClickExecutor {
    // All buttons must have unique IDs!
    companion object : ButtonClickExecutorDeclaration("counter")

    override suspend fun onClick(user: User, context: ComponentContext) {
        val newCount = counter.addAndGet()

        // The order is important here!
        // If you send the message first and then update the message, Discord will think that you want to edit the message that you sent!
        context.updateMessage(CounterExecutor.createCounterMessage(newCount))

        context.sendEphemeralMessage {
            content = "Increased the counter!"
        }
    }
}
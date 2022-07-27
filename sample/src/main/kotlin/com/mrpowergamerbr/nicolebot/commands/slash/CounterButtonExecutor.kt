package com.mrpowergamerbr.nicolebot.commands.slash

import com.mrpowergamerbr.nicolebot.utils.Counter
import net.perfectdreams.discordinteraktions.common.components.ButtonExecutor
import net.perfectdreams.discordinteraktions.common.components.ButtonExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.components.ComponentContext
import net.perfectdreams.discordinteraktions.common.entities.User

class CounterButtonExecutor(private val counter: Counter) : ButtonExecutor {
    // All buttons must have unique IDs!
    companion object : ButtonExecutorDeclaration("counter")

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
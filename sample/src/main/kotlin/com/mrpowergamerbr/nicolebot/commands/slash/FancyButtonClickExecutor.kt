package com.mrpowergamerbr.nicolebot.commands.slash

import dev.kord.core.entity.User
import net.perfectdreams.discordinteraktions.common.components.ButtonExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.components.ButtonExecutor
import net.perfectdreams.discordinteraktions.common.components.ComponentContext

class FancyButtonClickExecutor : ButtonExecutor {
    // All buttons must have unique IDs!
    companion object : ButtonExecutorDeclaration("fancy_button")

    override suspend fun onClick(user: User, context: ComponentContext) {
        context.sendEphemeralMessage {
            content = "The data on the button is `${context.data}`, wow!"
        }
    }
}
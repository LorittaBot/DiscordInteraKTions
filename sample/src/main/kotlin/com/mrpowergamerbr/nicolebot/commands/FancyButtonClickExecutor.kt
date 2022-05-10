package com.mrpowergamerbr.nicolebot.commands

import net.perfectdreams.discordinteraktions.common.components.ButtonClickExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.components.ButtonClickWithDataExecutor
import net.perfectdreams.discordinteraktions.common.components.ComponentContext
import net.perfectdreams.discordinteraktions.common.entities.User

class FancyButtonClickExecutor : ButtonClickWithDataExecutor {
    // All buttons must have unique IDs!
    companion object : ButtonClickExecutorDeclaration("fancy_button")

    override suspend fun onClick(user: User, context: ComponentContext, data: String) {
        context.sendEphemeralMessage {
            content = "The data on the button is `$data`, wow!"
        }
    }
}
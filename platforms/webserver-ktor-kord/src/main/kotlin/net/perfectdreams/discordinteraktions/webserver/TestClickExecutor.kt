package net.perfectdreams.discordinteraktions.webserver

import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.components.buttons.ButtonClickExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.components.buttons.ButtonClickWithNoDataExecutor
import net.perfectdreams.discordinteraktions.common.context.buttons.ButtonClickContext

class TestClickExecutor : ButtonClickWithNoDataExecutor {
    companion object : ButtonClickExecutorDeclaration(TestClickExecutor::class, "test_click")

    override suspend fun onClick(user: User, context: ButtonClickContext) {
        context.sendMessage {
            content = "Você clicou no botão! Uau!!"
        }
    }
}
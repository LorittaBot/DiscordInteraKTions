package net.perfectdreams.discordinteraktions.webserver

import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.components.buttons.ButtonClickExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.components.buttons.ButtonClickWithNoDataExecutor
import net.perfectdreams.discordinteraktions.common.context.buttons.ButtonClickContext
import java.io.File

class TestClickExecutor : ButtonClickWithNoDataExecutor {
    companion object : ButtonClickExecutorDeclaration(TestClickExecutor::class, "test_click")

    override suspend fun onClick(user: User, context: ButtonClickContext) {
        context.updateMessage {
            content = "owo whats this???"

            removeAlreadyUploadedFiles = true
            file("gessy_pistola.png", File("L:\\Pictures\\monke-oil-sticker.png").inputStream())
        }
    }
}
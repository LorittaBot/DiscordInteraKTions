package net.perfectdreams.discordinteraktions.webserver

import dev.kord.rest.builder.message.modify.allowedMentions
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.builder.message.allowedMentions
import net.perfectdreams.discordinteraktions.common.components.buttons.ButtonClickExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.components.buttons.ButtonClickWithNoDataExecutor
import net.perfectdreams.discordinteraktions.common.context.components.ComponentContext

class TestClickExecutor : ButtonClickWithNoDataExecutor {
    companion object : ButtonClickExecutorDeclaration(TestClickExecutor::class, "test_click")

    var mutex = Mutex()
    var count = 0

    override suspend fun onClick(user: User, context: ComponentContext) {
        mutex.withLock {
            count++
            context.updateMessage {
                content = "owo whats this??? Contagem: ${count}, último que clicou: <@${user.id.value}>"

                allowedMentions {} // Nothing
            }
        }

        context.sendEphemeralMessage {
            content = "seu voto foi contabilizad safad lets gooo"
        }
    }
}
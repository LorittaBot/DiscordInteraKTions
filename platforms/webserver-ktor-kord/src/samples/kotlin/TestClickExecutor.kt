import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.perfectdreams.discordinteraktions.common.entities.User
import net.perfectdreams.discordinteraktions.common.builder.message.allowedMentions
import net.perfectdreams.discordinteraktions.common.components.ButtonClickExecutor
import net.perfectdreams.discordinteraktions.common.components.ButtonClickExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.components.ComponentContext

class TestClickExecutor : ButtonClickExecutor {
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
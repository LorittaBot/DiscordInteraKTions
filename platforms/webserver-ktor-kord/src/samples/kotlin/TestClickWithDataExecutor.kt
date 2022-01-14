import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.components.buttons.ButtonClickExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.components.buttons.ButtonClickWithDataExecutor
import net.perfectdreams.discordinteraktions.common.context.components.ComponentContext

class TestClickWithDataExecutor : ButtonClickWithDataExecutor {
    companion object : ButtonClickExecutorDeclaration(TestClickWithDataExecutor::class, "test_click2")

    override suspend fun onClick(user: User, context: ComponentContext, data: String) {
        /* context.updateMessage {
            content = "deferred update owo"
        } */
    }
}
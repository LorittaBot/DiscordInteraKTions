package net.perfectdreams.discordinteraktions.webserver

import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.components.selects.SelectMenuExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.components.selects.SelectMenuWithDataExecutor
import net.perfectdreams.discordinteraktions.common.context.buttons.ButtonClickContext
import net.perfectdreams.discordinteraktions.common.context.selects.SelectMenuContext

class SelectExecutor : SelectMenuWithDataExecutor {
    companion object : SelectMenuExecutorDeclaration(SelectExecutor::class, "test_select")

    override suspend fun onSelect(user: User, context: SelectMenuContext, data: String, values: List<String>) {
        context.sendMessage {
            content = "<@${user.id.value}> selecionou $values"
        }
    }
}
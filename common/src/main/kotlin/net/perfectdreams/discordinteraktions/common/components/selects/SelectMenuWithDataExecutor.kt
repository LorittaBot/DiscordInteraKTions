package net.perfectdreams.discordinteraktions.common.components.selects

import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.context.buttons.ButtonClickContext
import net.perfectdreams.discordinteraktions.common.context.selects.SelectMenuContext

interface SelectMenuWithDataExecutor : SelectMenuExecutor {
    suspend fun onSelect(user: User, context: SelectMenuContext, data: String, values: List<String>)
}
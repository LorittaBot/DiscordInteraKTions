package net.perfectdreams.discordinteraktions.common.components.selects

import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.context.components.ComponentContext

interface SelectMenuWithDataExecutor : SelectMenuExecutor {
    suspend fun onSelect(user: User, context: ComponentContext, data: String, values: List<String>)
}
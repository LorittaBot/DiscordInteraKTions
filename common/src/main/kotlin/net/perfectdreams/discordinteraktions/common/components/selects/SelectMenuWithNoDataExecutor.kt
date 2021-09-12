package net.perfectdreams.discordinteraktions.common.components.selects

import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.context.components.ComponentContext

interface SelectMenuWithNoDataExecutor : SelectMenuExecutor {
    suspend fun onSelect(user: User, context: ComponentContext, values: List<String>)
}
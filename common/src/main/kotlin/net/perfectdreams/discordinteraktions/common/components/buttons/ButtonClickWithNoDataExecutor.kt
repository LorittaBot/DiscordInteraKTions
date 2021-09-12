package net.perfectdreams.discordinteraktions.common.components.buttons

import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.context.components.ComponentContext

interface ButtonClickWithNoDataExecutor : ButtonClickExecutor {
    suspend fun onClick(user: User, context: ComponentContext)
}
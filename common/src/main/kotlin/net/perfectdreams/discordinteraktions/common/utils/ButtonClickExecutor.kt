package net.perfectdreams.discordinteraktions.common.utils

import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.context.buttons.ButtonClickContext
import net.perfectdreams.discordinteraktions.common.context.commands.SlashCommandContext

interface ButtonClickExecutor<CustomButtonData> {
    /**
     * You should override this if you want to custom parse your data
     */
    suspend fun onClickConvertToProperData(user: User, context: ButtonClickContext, data: Any?) {
        onClick(user, context, data as CustomButtonData)
    }

    suspend fun onClick(user: User, context: ButtonClickContext, data: CustomButtonData)

    /**
     * Used by the [net.perfectdreams.discordinteraktions.common.buttons.ButtonStateManager] to match declarations to executors.
     *
     * By default the class of the executor is used, but this may cause issues when using anonymous classes!
     *
     * To avoid this issue, you can replace the signature with another unique identifier
     */
    fun signature(): Any = this::class
}
package net.perfectdreams.discordinteraktions.common.components

import net.perfectdreams.discordinteraktions.common.entities.User

sealed interface ComponentExecutor {
    /**
     * Used by the [ComponentExecutorDeclaration] to match declarations to executors.
     *
     * By default the class of the executor is used, but this may cause issues when using anonymous classes!
     *
     * To avoid this issue, you can replace the signature with another unique identifier
     */
    fun signature(): Any = this::class
}

// ===[ BUTTONS ]===
interface ButtonExecutor : ComponentExecutor {
    suspend fun onClick(user: User, context: ComponentContext)
}

// ===[ SELECT MENUS ]===
interface SelectMenuExecutor : ComponentExecutor {
    suspend fun onSelect(user: User, context: ComponentContext, values: List<String>)
}
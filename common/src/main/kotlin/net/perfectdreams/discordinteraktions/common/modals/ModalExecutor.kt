package net.perfectdreams.discordinteraktions.common.modals

import net.perfectdreams.discordinteraktions.common.modals.components.ModalArguments

interface ModalExecutor {
    suspend fun onSubmit(context: ModalContext, args: ModalArguments)

    /**
     * Used by the [ModalExecutorDeclaration] to match declarations to executors.
     *
     * By default the class of the executor is used, but this may cause issues when using anonymous classes!
     *
     * To avoid this issue, you can replace the signature with another unique identifier
     */
    fun signature(): Any = this::class
}
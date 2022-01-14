package net.perfectdreams.discordinteraktions.common.autocomplete

interface AutocompleteExecutor<T> {
    suspend fun onAutocomplete(focusedOption: FocusedCommandOption): Map<String, T>

    /**
     * Used by the [net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandExecutorDeclaration] to match declarations to executors.
     *
     * By default the class of the executor is used, but this may cause issues when using anonymous classes!
     *
     * To avoid this issue, you can replace the signature with another unique identifier
     */
    open fun signature(): Any = this::class
}
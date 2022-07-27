package net.perfectdreams.discordinteraktions.common.autocomplete

fun interface AutocompleteHandler<T> {
    suspend fun handle(context: AutocompleteContext, focusedOption: FocusedCommandOption): Map<String, T>
}
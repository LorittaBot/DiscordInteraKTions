package net.perfectdreams.discordinteraktions.common.autocomplete

sealed class AutocompleteExecutorDeclaration<T>(
    /**
     * The "parent" is Any to avoid issues with anonymous classes
     *
     * When using anonymous classes, you can use another type to match declarations
     */
    val parent: Any
)

open class StringAutocompleteExecutorDeclaration(parent: Any) : AutocompleteExecutorDeclaration<String>(parent)
open class IntegerAutocompleteExecutorDeclaration(parent: Any) : AutocompleteExecutorDeclaration<Long>(parent)
open class NumberAutocompleteExecutorDeclaration(parent: Any) : AutocompleteExecutorDeclaration<Double>(parent)
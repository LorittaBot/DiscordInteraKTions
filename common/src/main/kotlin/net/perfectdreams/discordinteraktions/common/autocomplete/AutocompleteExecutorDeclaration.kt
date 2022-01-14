package net.perfectdreams.discordinteraktions.common.autocomplete

open class AutocompleteExecutorDeclaration<T>(
    /**
     * The "parent" is Any to avoid issues with anonymous classes
     *
     * When using anonymous classes, you can use another type to match declarations
     */
    val parent: Any
)
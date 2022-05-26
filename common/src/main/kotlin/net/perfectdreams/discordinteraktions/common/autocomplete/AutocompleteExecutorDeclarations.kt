package net.perfectdreams.discordinteraktions.common.autocomplete

import net.perfectdreams.discordinteraktions.platforms.kord.commands.CommandDeclarationUtils

sealed class AutocompleteExecutorDeclaration<T>(
    /**
     * The [parent] is Any? to avoid issues with anonymous classes
     *
     * When using anonymous classes, you can use another type to match declarations
     *
     * If [parent] is null when the class is initialized, the declaration will try to find the parent by using Reflection!
     */
    var parent: Any? = null
) {
    init {
        if (parent == null)
            parent = CommandDeclarationUtils.getParentClass(this)
    }
}

open class StringAutocompleteExecutorDeclaration(parent: Any? = null) : AutocompleteExecutorDeclaration<String>(parent)
open class IntegerAutocompleteExecutorDeclaration(parent: Any? = null) : AutocompleteExecutorDeclaration<Long>(parent)
open class NumberAutocompleteExecutorDeclaration(parent: Any? = null) : AutocompleteExecutorDeclaration<Double>(parent)
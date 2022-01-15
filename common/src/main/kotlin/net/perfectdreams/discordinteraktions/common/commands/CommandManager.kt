package net.perfectdreams.discordinteraktions.common.commands

import net.perfectdreams.discordinteraktions.common.autocomplete.AutocompleteExecutor
import net.perfectdreams.discordinteraktions.common.autocomplete.AutocompleteExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.commands.interaction.InteractionCommandExecutor
import net.perfectdreams.discordinteraktions.common.components.buttons.ButtonClickExecutor
import net.perfectdreams.discordinteraktions.common.components.buttons.ButtonClickExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.components.selects.SelectMenuExecutor
import net.perfectdreams.discordinteraktions.common.components.selects.SelectMenuExecutorDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.InteractionCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.wrappers.InteractionCommandDeclarationWrapper

open class CommandManager {
    val declarations = mutableListOf<InteractionCommandDeclaration>()
    val executors = mutableListOf<InteractionCommandExecutor>()

    val buttonDeclarations = mutableListOf<ButtonClickExecutorDeclaration>()
    val buttonExecutors = mutableListOf<ButtonClickExecutor>()

    val selectMenusDeclarations = mutableListOf<SelectMenuExecutorDeclaration>()
    val selectMenusExecutors = mutableListOf<SelectMenuExecutor>()

    val autocompleteDeclarations = mutableListOf<AutocompleteExecutorDeclaration<*>>()
    val autocompleteExecutors = mutableListOf<AutocompleteExecutor<*>>()

    val componentDeclarations: List<String>
        get() = buttonDeclarations.map { it.id } + selectMenusDeclarations.map { it.id }

    fun register(declarationWrapper: InteractionCommandDeclarationWrapper, vararg executors: InteractionCommandExecutor) {
        val declaration = declarationWrapper.declaration()

        if (declarations.any { it.name == declaration.name })
            error("There's already an root command registered with the label ${declaration.name}!")

        declarations.add(declaration)
        this.executors.addAll(executors)
    }

    fun register(declaration: ButtonClickExecutorDeclaration, executor: ButtonClickExecutor) {
        if (componentDeclarations.any { it == declaration.id })
            error("There's already a component executor registered with the ID ${declaration.id}!")

        buttonDeclarations.add(declaration)
        buttonExecutors.add(executor)
    }

    fun register(declaration: SelectMenuExecutorDeclaration, executor: SelectMenuExecutor) {
        if (componentDeclarations.any { it == declaration.id })
            error("There's already a component executor registered with the ID ${declaration.id}!")

        selectMenusDeclarations.add(declaration)
        selectMenusExecutors.add(executor)
    }

    fun <T> register(declaration: AutocompleteExecutorDeclaration<T>, executor: AutocompleteExecutor<T>) {
        autocompleteDeclarations.add(declaration)
        autocompleteExecutors.add(executor)
    }
}
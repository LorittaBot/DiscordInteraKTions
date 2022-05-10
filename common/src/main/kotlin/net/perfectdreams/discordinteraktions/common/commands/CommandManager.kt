package net.perfectdreams.discordinteraktions.common.commands

import net.perfectdreams.discordinteraktions.common.autocomplete.AutocompleteExecutor
import net.perfectdreams.discordinteraktions.common.autocomplete.AutocompleteExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.components.ButtonClickBaseExecutor
import net.perfectdreams.discordinteraktions.common.components.ButtonClickExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.components.SelectMenuBaseExecutor
import net.perfectdreams.discordinteraktions.common.components.SelectMenuExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.modals.ModalSubmitBaseExecutor
import net.perfectdreams.discordinteraktions.common.modals.ModalSubmitExecutorDeclaration

open class CommandManager {
    val applicationCommandsDeclarations = mutableListOf<ApplicationCommandDeclaration>()
    val applicationCommandsExecutors = mutableListOf<ApplicationCommandExecutor>()

    val buttonDeclarations = mutableListOf<ButtonClickExecutorDeclaration>()
    val buttonExecutors = mutableListOf<ButtonClickBaseExecutor>()

    val selectMenusDeclarations = mutableListOf<SelectMenuExecutorDeclaration>()
    val selectMenusExecutors = mutableListOf<SelectMenuBaseExecutor>()

    val autocompleteDeclarations = mutableListOf<AutocompleteExecutorDeclaration<*>>()
    val autocompleteExecutors = mutableListOf<AutocompleteExecutor<*>>()

    val modalSubmitDeclarations = mutableListOf<ModalSubmitExecutorDeclaration>()
    val modalSubmitExecutors = mutableListOf<ModalSubmitBaseExecutor>()

    val componentDeclarations: List<String>
        get() = buttonDeclarations.map { it.id } + selectMenusDeclarations.map { it.id }

    fun register(declarationWrapper: ApplicationCommandDeclarationWrapper, vararg executors: ApplicationCommandExecutor) =
        register(declarationWrapper.declaration(), *executors)

    fun register(declaration: ApplicationCommandDeclaration, vararg executors: ApplicationCommandExecutor) {
        // TODO: Validate if all executors of the command are present
        if (applicationCommandsDeclarations.any { it.name == declaration.name })
            error("There's already an root command registered with the label ${declaration.name}!")

        applicationCommandsDeclarations.add(declaration)
        this.applicationCommandsExecutors.addAll(executors)
    }

    fun register(declaration: ButtonClickExecutorDeclaration, executor: ButtonClickBaseExecutor) {
        if (componentDeclarations.any { it == declaration.id })
            error("There's already a component executor registered with the ID ${declaration.id}!")

        buttonDeclarations.add(declaration)
        buttonExecutors.add(executor)
    }

    fun register(declaration: SelectMenuExecutorDeclaration, executor: SelectMenuBaseExecutor) {
        if (componentDeclarations.any { it == declaration.id })
            error("There's already a component executor registered with the ID ${declaration.id}!")

        selectMenusDeclarations.add(declaration)
        selectMenusExecutors.add(executor)
    }

    fun <T> register(declaration: AutocompleteExecutorDeclaration<T>, executor: AutocompleteExecutor<T>) {
        autocompleteDeclarations.add(declaration)
        autocompleteExecutors.add(executor)
    }

    fun register(declaration: ModalSubmitExecutorDeclaration, executor: ModalSubmitBaseExecutor) {
        modalSubmitDeclarations.add(declaration)
        modalSubmitExecutors.add(executor)
    }
}
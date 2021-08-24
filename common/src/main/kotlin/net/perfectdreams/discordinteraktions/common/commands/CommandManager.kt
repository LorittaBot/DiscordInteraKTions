package net.perfectdreams.discordinteraktions.common.commands

import net.perfectdreams.discordinteraktions.common.commands.interaction.InteractionCommandExecutor
import net.perfectdreams.discordinteraktions.common.components.buttons.ButtonClickExecutor
import net.perfectdreams.discordinteraktions.common.components.buttons.ButtonClickExecutorDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.InteractionCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.wrappers.InteractionCommandDeclarationWrapper

open class CommandManager {
    val declarations = mutableListOf<InteractionCommandDeclaration>()
    val executors = mutableListOf<InteractionCommandExecutor>()

    val buttonDeclarations = mutableListOf<ButtonClickExecutorDeclaration>()
    val buttonExecutors = mutableListOf<ButtonClickExecutor>()

    fun register(declarationWrapper: InteractionCommandDeclarationWrapper, vararg executors: InteractionCommandExecutor) {
        val declaration = declarationWrapper.declaration()

        if (declarations.any { it.name == declaration.name })
            error("There's already an root command registered with the label ${declaration.name}!")

        declarations.add(declaration)
        this.executors.addAll(executors)
    }

    fun register(declaration: ButtonClickExecutorDeclaration, executor: ButtonClickExecutor) {
        if (declarations.any { it.name == declaration.id })
            error("There's already a button executor registered with the ID ${declaration.id}!")

        buttonDeclarations.add(declaration)
        buttonExecutors.add(executor)
    }
}
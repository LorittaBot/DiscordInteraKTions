package net.perfectdreams.discordinteraktions.common.commands

import net.perfectdreams.discordinteraktions.common.commands.interaction.InteractionCommandExecutor
import net.perfectdreams.discordinteraktions.declarations.commands.InteractionCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.wrappers.InteractionCommandDeclarationWrapper

open class CommandManager {
    val declarations = mutableListOf<InteractionCommandDeclaration>()
    val executors = mutableListOf<InteractionCommandExecutor>()

    fun register(declaration: InteractionCommandDeclarationWrapper, vararg executors: InteractionCommandExecutor) {
        declarations.add(declaration.declaration())
        this.executors.addAll(executors)
    }
}
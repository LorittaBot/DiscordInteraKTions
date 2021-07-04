package net.perfectdreams.discordinteraktions.common.commands

import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandDeclarationBuilder

open class CommandManager {
    val declarations = mutableListOf<SlashCommandDeclarationBuilder>()
    val executors = mutableListOf<SlashCommandExecutor>()

    fun register(declaration: SlashCommandDeclaration, vararg executors: SlashCommandExecutor) {
        declarations.add(declaration.declaration())
        this.executors.addAll(executors)
    }
}
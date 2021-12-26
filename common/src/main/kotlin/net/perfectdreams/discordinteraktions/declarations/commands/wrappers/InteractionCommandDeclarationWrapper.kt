package net.perfectdreams.discordinteraktions.declarations.commands.wrappers

import net.perfectdreams.discordinteraktions.declarations.commands.InteractionCommandDeclaration

interface InteractionCommandDeclarationWrapper {
    fun declaration(): InteractionCommandDeclaration
}
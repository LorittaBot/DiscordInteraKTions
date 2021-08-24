package net.perfectdreams.discordinteraktions.declarations.commands.wrappers

import net.perfectdreams.discordinteraktions.declarations.commands.MessageCommandDeclaration

interface MessageCommandDeclarationWrapper : InteractionCommandDeclarationWrapper {
    override fun declaration(): MessageCommandDeclaration
}
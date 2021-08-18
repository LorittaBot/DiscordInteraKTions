package net.perfectdreams.discordinteraktions.declarations.commands.wrappers

import net.perfectdreams.discordinteraktions.declarations.commands.ApplicationCommandDeclaration

interface ApplicationCommandDeclarationWrapper : InteractionCommandDeclarationWrapper {
    override fun declaration(): ApplicationCommandDeclaration
}
package net.perfectdreams.discordinteraktions.declarations.commands.wrappers

import net.perfectdreams.discordinteraktions.declarations.commands.SlashCommandDeclaration

interface SlashCommandDeclarationWrapper : InteractionCommandDeclarationWrapper {
    override fun declaration(): SlashCommandDeclaration
}
package net.perfectdreams.discordinteraktions.declarations.commands

/**
 * Base class of every interaction declaration, because all interactions share a [name]
 */
sealed class InteractionCommandDeclaration(
    val name: String
)
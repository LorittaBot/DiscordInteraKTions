package net.perfectdreams.discordinteraktions.declarations.commands

/**
 * Base class of every interaction declaration, because all interactions share a [name] and a [description]
 */
sealed class InteractionCommandDeclaration(
    val name: String,
    val description: String
)
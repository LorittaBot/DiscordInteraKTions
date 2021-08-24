package net.perfectdreams.discordinteraktions.declarations.commands.message

import net.perfectdreams.discordinteraktions.declarations.commands.application.ApplicationCommandExecutorDeclaration

// The "parent" is Any to avoid issues with anonymous classes
// When using anonymous classes, you can use another type to match declarations
open class MessageCommandExecutorDeclaration(parent: Any) : ApplicationCommandExecutorDeclaration(parent)
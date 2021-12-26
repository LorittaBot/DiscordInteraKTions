package net.perfectdreams.discordinteraktions.declarations.commands.user

import net.perfectdreams.discordinteraktions.declarations.commands.application.ApplicationCommandExecutorDeclaration

// The "parent" is Any to avoid issues with anonymous classes
// When using anonymous classes, you can use another type to match declarations
open class UserCommandExecutorDeclaration(parent: Any) : ApplicationCommandExecutorDeclaration(parent)
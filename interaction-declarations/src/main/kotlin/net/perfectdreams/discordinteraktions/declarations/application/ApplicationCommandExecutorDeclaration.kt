package net.perfectdreams.discordinteraktions.declarations.slash

import net.perfectdreams.discordinteraktions.declarations.slash.options.CommandOptions

// The "parent" is Any to avoid issues with anonymous classes
// When using anonymous classes, you can use another type to match declarations
open class ApplicationCommandExecutorDeclaration(val parent: Any) {
    open val options: CommandOptions = CommandOptions.NO_OPTIONS
}
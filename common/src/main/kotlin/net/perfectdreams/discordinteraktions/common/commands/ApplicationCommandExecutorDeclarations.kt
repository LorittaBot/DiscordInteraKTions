package net.perfectdreams.discordinteraktions.common.commands

import net.perfectdreams.discordinteraktions.common.commands.options.ApplicationCommandOptions

// The "parent" is Any to avoid issues with anonymous classes
// When using anonymous classes, you can use another type to match declarations
sealed class ApplicationCommandExecutorDeclaration(val parent: Any)

open class SlashCommandExecutorDeclaration(parent: Any) : ApplicationCommandExecutorDeclaration(parent) {
    open val options: ApplicationCommandOptions = ApplicationCommandOptions.NO_OPTIONS
}

open class UserCommandExecutorDeclaration(parent: Any) : ApplicationCommandExecutorDeclaration(parent)

open class MessageCommandExecutorDeclaration(parent: Any) : ApplicationCommandExecutorDeclaration(parent)
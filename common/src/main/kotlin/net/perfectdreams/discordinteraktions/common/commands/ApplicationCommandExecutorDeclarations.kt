package net.perfectdreams.discordinteraktions.common.commands

import net.perfectdreams.discordinteraktions.common.commands.options.ApplicationCommandOptions
import net.perfectdreams.discordinteraktions.platforms.kord.commands.CommandDeclarationUtils

/**
 * The [parent] is Any? to avoid issues with anonymous classes
 *
 * When using anonymous classes, you can use another type to match declarations
 *
 * If [parent] is null when the class is initialized, the declaration will try to find the parent by using Reflection!
 */
sealed class ApplicationCommandExecutorDeclaration(var parent: Any? = null) {
    init {
        if (parent == null)
            parent = CommandDeclarationUtils.getParentClass(this)
    }
}

open class SlashCommandExecutorDeclaration(parent: Any? = null) : ApplicationCommandExecutorDeclaration(parent) {
    open val options: ApplicationCommandOptions = ApplicationCommandOptions.NO_OPTIONS
}

open class UserCommandExecutorDeclaration(parent: Any? = null) : ApplicationCommandExecutorDeclaration(parent)

open class MessageCommandExecutorDeclaration(parent: Any? = null) : ApplicationCommandExecutorDeclaration(parent)
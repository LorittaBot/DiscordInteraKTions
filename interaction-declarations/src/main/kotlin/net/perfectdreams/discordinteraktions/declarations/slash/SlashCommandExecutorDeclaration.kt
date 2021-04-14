package net.perfectdreams.discordinteraktions.declarations.slash

import net.perfectdreams.discordinteraktions.declarations.slash.options.CommandOptions
import kotlin.reflect.KClass

open class SlashCommandExecutorDeclaration(val parent: KClass<*>) {
    open val options: CommandOptions = CommandOptions.NO_OPTIONS
}
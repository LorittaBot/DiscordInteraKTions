package net.perfectdreams.discordinteraktions.commands

import net.perfectdreams.discordinteraktions.context.SlashCommandContext
import net.perfectdreams.discordinteraktions.declarations.slash.SlashCommandDeclaration

abstract class SlashCommand(val declaration: SlashCommandDeclaration) {
    abstract suspend fun executes(context: SlashCommandContext)
}
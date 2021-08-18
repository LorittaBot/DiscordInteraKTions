package net.perfectdreams.discordinteraktions.declarations.commands

import net.perfectdreams.discordinteraktions.declarations.commands.slash.SlashCommandExecutorDeclaration

class SlashCommandDeclaration(
    name: String,
    description: String,
    val executor: SlashCommandExecutorDeclaration? = null,
    val subcommands: List<SlashCommandDeclaration>,
    val subcommandGroups: List<SlashCommandGroupDeclaration>
) : InteractionCommandDeclaration(name, description)
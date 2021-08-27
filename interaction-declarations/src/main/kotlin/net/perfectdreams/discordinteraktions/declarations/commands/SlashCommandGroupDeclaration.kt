package net.perfectdreams.discordinteraktions.declarations.commands

class SlashCommandGroupDeclaration(
    name: String,
    val description: String,
    val subcommands: List<SlashCommandDeclaration>
) : InteractionCommandDeclaration(name)
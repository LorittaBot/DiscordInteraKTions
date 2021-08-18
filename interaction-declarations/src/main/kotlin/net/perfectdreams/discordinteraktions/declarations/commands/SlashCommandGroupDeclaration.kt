package net.perfectdreams.discordinteraktions.declarations.commands

class SlashCommandGroupDeclaration(
    name: String,
    description: String,
    val subcommands: List<SlashCommandDeclaration>
) : InteractionCommandDeclaration(name, description)
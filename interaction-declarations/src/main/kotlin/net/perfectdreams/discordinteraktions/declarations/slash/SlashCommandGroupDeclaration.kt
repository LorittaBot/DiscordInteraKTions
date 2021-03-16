package net.perfectdreams.discordinteraktions.declarations.slash

abstract class SlashCommandGroupDeclaration(
    val name: String,
    val description: String
) {
    val subcommands = mutableListOf<SlashCommandDeclaration>()

    fun <T : SlashCommandDeclaration> subcommand(declaration: T): T {
        return declaration
    }

    fun <T : SlashCommandDeclaration> T.register(): T {
        subcommands.add(this)
        return this
    }
}
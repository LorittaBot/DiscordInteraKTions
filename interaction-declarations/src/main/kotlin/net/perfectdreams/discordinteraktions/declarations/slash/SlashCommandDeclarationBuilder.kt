package net.perfectdreams.discordinteraktions.declarations.slash

fun slashCommand(name: String, description: String, block: SlashCommandDeclarationBuilder.() -> (Unit)): SlashCommandDeclarationBuilder {
    return SlashCommandDeclarationBuilder(name, description).apply(block)
}

class SlashCommandDeclarationBuilder(val name: String, val description: String) {
    var executor: SlashCommandExecutorDeclaration? = null
    val subcommands = mutableListOf<SlashCommandDeclarationBuilder>()
    val subcommandGroups = mutableListOf<SlashCommandGroupDeclarationBuilder>()

    fun subcommand(name: String, description: String, block: SlashCommandDeclarationBuilder.() -> (Unit)) {
        subcommands += SlashCommandDeclarationBuilder(name, description).apply(block)
    }

    fun subcommandGroup(name: String, description: String, block: SlashCommandGroupDeclarationBuilder.() -> (Unit)) {
        subcommandGroups += SlashCommandGroupDeclarationBuilder(name, description).apply(block)
    }
}

class SlashCommandGroupDeclarationBuilder(val name: String, val description: String) {
    // Groups can't have executors!
    val subcommands = mutableListOf<SlashCommandDeclarationBuilder>()

    fun subcommand(name: String, description: String, block: SlashCommandDeclarationBuilder.() -> (Unit)) {
        subcommands += SlashCommandDeclarationBuilder(name, description).apply(block)
    }
}
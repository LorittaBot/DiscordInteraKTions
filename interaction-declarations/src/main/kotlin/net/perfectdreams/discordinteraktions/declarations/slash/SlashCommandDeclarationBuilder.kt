package net.perfectdreams.discordinteraktions.declarations.slash

fun slashCommand(name: String, block: SlashCommandDeclarationBuilder.() -> (Unit)): SlashCommandDeclarationBuilder {
    return SlashCommandDeclarationBuilder(name).apply(block)
}

class SlashCommandDeclarationBuilder(val name: String) {
    var description: String? = null
    var executor: SlashCommandExecutorDeclaration? = null
    val subcommands = mutableListOf<SlashCommandDeclarationBuilder>()
    val subcommandGroups = mutableListOf<SlashCommandGroupDeclarationBuilder>()

    fun subcommand(name: String, block: SlashCommandDeclarationBuilder.() -> (Unit)) {
        subcommands += SlashCommandDeclarationBuilder(name).apply(block)
    }

    fun subcommandGroup(name: String, block: SlashCommandGroupDeclarationBuilder.() -> (Unit)) {
        subcommandGroups += SlashCommandGroupDeclarationBuilder(name).apply(block)
    }
}

class SlashCommandGroupDeclarationBuilder(val name: String) {
    var description: String? = null

    // Groups can't have executors!
    val subcommands = mutableListOf<SlashCommandDeclarationBuilder>()

    fun subcommand(name: String, block: SlashCommandDeclarationBuilder.() -> (Unit)) {
        subcommands += SlashCommandDeclarationBuilder(name).apply(block)
    }
}
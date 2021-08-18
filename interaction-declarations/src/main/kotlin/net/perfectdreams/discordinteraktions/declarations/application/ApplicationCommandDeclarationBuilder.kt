package net.perfectdreams.discordinteraktions.declarations.slash

fun applicationCommand(name: String, description: String, block: ApplicationCommandDeclarationBuilder.() -> (Unit)): ApplicationCommandDeclarationBuilder {
    return ApplicationCommandDeclarationBuilder(name, description).apply(block)
}

class ApplicationCommandDeclarationBuilder(val name: String, val description: String) {
    var executor: ApplicationCommandExecutorDeclaration? = null
    val subcommands = mutableListOf<ApplicationCommandDeclarationBuilder>()
    val subcommandGroups = mutableListOf<ApplicationCommandGroupDeclarationBuilder>()

    fun subcommand(name: String, description: String, block: ApplicationCommandDeclarationBuilder.() -> (Unit)) {
        subcommands += ApplicationCommandDeclarationBuilder(name, description).apply(block)
    }

    fun subcommandGroup(name: String, description: String, block: ApplicationCommandGroupDeclarationBuilder.() -> (Unit)) {
        subcommandGroups += ApplicationCommandGroupDeclarationBuilder(name, description).apply(block)
    }
}

class ApplicationCommandGroupDeclarationBuilder(val name: String, val description: String) {
    // Groups can't have executors!
    val subcommands = mutableListOf<ApplicationCommandDeclarationBuilder>()

    fun subcommand(name: String, description: String, block: ApplicationCommandDeclarationBuilder.() -> (Unit)) {
        subcommands += ApplicationCommandDeclarationBuilder(name, description).apply(block)
    }
}
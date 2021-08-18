package net.perfectdreams.discordinteraktions.declarations.commands.slash

import net.perfectdreams.discordinteraktions.declarations.commands.SlashCommandDeclaration
import net.perfectdreams.discordinteraktions.declarations.commands.SlashCommandGroupDeclaration

fun slashCommand(name: String, description: String, block: SlashCommandDeclarationBuilder.() -> (Unit)): SlashCommandDeclaration {
    return SlashCommandDeclarationBuilder(name, description)
        .apply(block)
        .build()
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

    fun build(): SlashCommandDeclaration {
        return SlashCommandDeclaration(
            name,
            description,
            executor,
            subcommands.map { it.build() },
            subcommandGroups.map { it.build() }
        )
    }
}

class SlashCommandGroupDeclarationBuilder(val name: String, val description: String) {
    // Groups can't have executors!
    val subcommands = mutableListOf<SlashCommandDeclarationBuilder>()

    fun subcommand(name: String, description: String, block: SlashCommandDeclarationBuilder.() -> (Unit)) {
        subcommands += SlashCommandDeclarationBuilder(name, description).apply(block)
    }

    fun build(): SlashCommandGroupDeclaration {
        return SlashCommandGroupDeclaration(
            name,
            description,
            subcommands.map { it.build() }
        )
    }
}
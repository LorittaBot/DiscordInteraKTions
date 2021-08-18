package net.perfectdreams.discordinteraktions.declarations.commands

import net.perfectdreams.discordinteraktions.declarations.commands.ApplicationCommandDeclaration

fun applicationCommand(name: String, description: String, executor: ApplicationCommandExecutorDeclaration, block: ApplicationCommandDeclarationBuilder.() -> (Unit)): ApplicationCommandDeclarationBuilder {
    return ApplicationCommandDeclarationBuilder(name, description, executor).apply(block)
}

class ApplicationCommandDeclarationBuilder(val name: String, val description: String, val executor: ApplicationCommandExecutorDeclaration) {
    fun build(): ApplicationCommandDeclaration {
        return ApplicationCommandDeclaration(
            name,
            description,
            executor
        )
    }
}
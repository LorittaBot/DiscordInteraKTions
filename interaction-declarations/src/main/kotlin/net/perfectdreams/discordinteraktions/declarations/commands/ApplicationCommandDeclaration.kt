package net.perfectdreams.discordinteraktions.declarations.commands

import net.perfectdreams.discordinteraktions.declarations.commands.application.ApplicationCommandExecutorDeclaration

class ApplicationCommandDeclaration(
    name: String,
    description: String,
    val executor: ApplicationCommandExecutorDeclaration // User/Message commands always requires an executor, that's why it is not nullable!
) : InteractionCommandDeclaration(name, description)
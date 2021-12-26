package net.perfectdreams.discordinteraktions.declarations.commands

import net.perfectdreams.discordinteraktions.declarations.commands.user.UserCommandExecutorDeclaration

class UserCommandDeclaration(
    name: String,
    val executor: UserCommandExecutorDeclaration // User/Message commands always requires an executor, that's why it is not nullable!
) : InteractionCommandDeclaration(name)
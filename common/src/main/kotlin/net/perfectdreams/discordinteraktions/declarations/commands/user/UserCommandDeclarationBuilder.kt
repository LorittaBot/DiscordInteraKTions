package net.perfectdreams.discordinteraktions.declarations.commands.user

import net.perfectdreams.discordinteraktions.declarations.commands.UserCommandDeclaration

fun userCommand(name: String, executor: UserCommandExecutorDeclaration): UserCommandDeclaration {
    return UserCommandDeclarationBuilder(name, executor).build()
}

class UserCommandDeclarationBuilder(val name: String, val executor: UserCommandExecutorDeclaration) {
    fun build(): UserCommandDeclaration {
        return UserCommandDeclaration(
            name,
            executor
        )
    }
}
package net.perfectdreams.discordinteraktions.declarations.commands.message

import net.perfectdreams.discordinteraktions.declarations.commands.MessageCommandDeclaration

fun messageCommand(name: String, executor: MessageCommandExecutorDeclaration): MessageCommandDeclaration {
    return MessageCommandDeclarationBuilder(name, executor).build()
}

class MessageCommandDeclarationBuilder(val name: String, val executor: MessageCommandExecutorDeclaration) {
    fun build(): MessageCommandDeclaration {
        return MessageCommandDeclaration(
            name,
            executor
        )
    }
}
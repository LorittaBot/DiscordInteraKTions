package net.perfectdreams.discordinteraktions.common.commands

// ===[ SLASH COMMANDS ]===
fun slashCommand(name: String, description: String, block: SlashCommandDeclarationBuilder.() -> (Unit)): SlashCommandDeclaration {
    return SlashCommandDeclarationBuilder(name, description)
        .apply(block)
        .build()
}

class SlashCommandDeclarationBuilder(val name: String, val description: String) {
    var executor: SlashCommandExecutorDeclaration? = null
    val subcommands = mutableListOf<SlashCommandDeclaration>()
    val subcommandGroups = mutableListOf<SlashCommandGroupDeclaration>()

    fun subcommand(name: String, description: String, block: SlashCommandDeclarationBuilder.() -> (Unit)) {
        subcommands += SlashCommandDeclarationBuilder(name, description).apply(block)
            .build()
    }

    fun subcommandGroup(name: String, description: String, block: SlashCommandGroupDeclarationBuilder.() -> (Unit)) {
        subcommandGroups += SlashCommandGroupDeclarationBuilder(name, description).apply(block)
            .build()
    }

    fun build(): SlashCommandDeclaration {
        return SlashCommandDeclaration(
            name,
            description,
            executor,
            subcommands,
            subcommandGroups
        )
    }
}

class SlashCommandGroupDeclarationBuilder(val name: String, val description: String) {
    // Groups can't have executors!
    val subcommands = mutableListOf<SlashCommandDeclaration>()

    fun subcommand(name: String, description: String, block: SlashCommandDeclarationBuilder.() -> (Unit)) {
        subcommands += SlashCommandDeclarationBuilder(name, description).apply(block).build()
    }

    fun build(): SlashCommandGroupDeclaration {
        return SlashCommandGroupDeclaration(
            name,
            description,
            subcommands
        )
    }
}

// ===[ USER COMMANDS ]===
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

// ===[ MESSAGE COMMANDS ]===
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
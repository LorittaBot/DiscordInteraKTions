package net.perfectdreams.discordinteraktions.common.commands

/**
 * Base class of every application declaration, because all interactions share a [name]
 */
sealed class ApplicationCommandDeclaration(
    val name: String
)

class SlashCommandDeclaration(
    name: String,
    val description: String,
    val executor: SlashCommandExecutorDeclaration? = null,
    val subcommands: List<SlashCommandDeclaration>,
    val subcommandGroups: List<SlashCommandGroupDeclaration>
) : ApplicationCommandDeclaration(name)

class SlashCommandGroupDeclaration(
    name: String,
    val description: String,
    val subcommands: List<SlashCommandDeclaration>
) : ApplicationCommandDeclaration(name)

class UserCommandDeclaration(
    name: String,
    val executor: UserCommandExecutorDeclaration // User/Message commands always requires an executor, that's why it is not nullable!
) : ApplicationCommandDeclaration(name)

class MessageCommandDeclaration(
    name: String,
    val executor: MessageCommandExecutorDeclaration // User/Message commands always requires an executor, that's why it is not nullable!
) : ApplicationCommandDeclaration(name)
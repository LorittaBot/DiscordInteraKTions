package net.perfectdreams.discordinteraktions.common.commands

import dev.kord.common.Locale
import dev.kord.common.entity.Permissions

/**
 * Base class of every application declaration, because all interactions share a [name]
 */
sealed class ApplicationCommandDeclaration {
    abstract val name: String
    abstract val nameLocalizations: Map<Locale, String>?
}

abstract class SlashCommandDeclaration : ApplicationCommandDeclaration() {
    abstract val description: String
    abstract val descriptionLocalizations: Map<Locale, String>?
    abstract val executor: SlashCommandExecutor?
    abstract val defaultMemberPermissions: Permissions?
    abstract val dmPermission: Boolean?
    abstract val subcommands: List<SlashCommandDeclaration>
    abstract val subcommandGroups: List<SlashCommandGroupDeclaration>
}

abstract class SlashCommandGroupDeclaration : ApplicationCommandDeclaration() {
    abstract val description: String
    abstract val descriptionLocalizations: Map<Locale, String>?
    abstract val subcommands: List<SlashCommandDeclaration>
}

abstract class UserCommandDeclaration : ApplicationCommandDeclaration() {
    abstract val defaultMemberPermissions: Permissions?
    abstract val executor: UserCommandExecutor // User/Message commands always requires an executor, that's why it is not nullable!
    abstract val dmPermission: Boolean?
}

abstract class MessageCommandDeclaration : ApplicationCommandDeclaration() {
    abstract val defaultMemberPermissions: Permissions?
    abstract val executor: MessageCommandExecutor // User/Message commands always requires an executor, that's why it is not nullable!
    abstract val dmPermission: Boolean?
}

// ===[ DEFAULT IMPLEMENTATIONS ]===
class InteraKTionsSlashCommandDeclaration(
    override val name: String,
    override val nameLocalizations: Map<Locale, String>? = null,
    override val description: String,
    override val descriptionLocalizations: Map<Locale, String>? = null,
    override val executor: SlashCommandExecutor? = null,
    override val defaultMemberPermissions: Permissions?,
    override val dmPermission: Boolean?,
    override val subcommands: List<SlashCommandDeclaration>,
    override val subcommandGroups: List<SlashCommandGroupDeclaration>
) : SlashCommandDeclaration()

class InteraKTionsSlashCommandGroupDeclaration(
    override val name: String,
    override val nameLocalizations: Map<Locale, String>? = null,
    override val description: String,
    override val descriptionLocalizations: Map<Locale, String>? = null,
    override val subcommands: List<SlashCommandDeclaration>
) : SlashCommandGroupDeclaration()

class InteraKTionsUserCommandDeclaration(
    override val name: String,
    override val nameLocalizations: Map<Locale, String>? = null,
    override val executor: UserCommandExecutor,
    override val defaultMemberPermissions: Permissions?,
    override val dmPermission: Boolean?
) : UserCommandDeclaration()

class InteraKTionsMessageCommandDeclaration(
    override val name: String,
    override val nameLocalizations: Map<Locale, String>? = null,
    override val executor: MessageCommandExecutor,
    override val defaultMemberPermissions: Permissions?,
    override val dmPermission: Boolean?
) : MessageCommandDeclaration()
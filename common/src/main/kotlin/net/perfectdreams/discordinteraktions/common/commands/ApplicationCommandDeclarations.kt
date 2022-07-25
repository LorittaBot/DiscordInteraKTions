package net.perfectdreams.discordinteraktions.common.commands

import dev.kord.common.Locale
import dev.kord.common.entity.Permissions
import net.perfectdreams.discordinteraktions.common.commands.options.DiscordCommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.InteraKTionsCommandOption

/**
 * Base class of every application declaration, because all interactions share a [name]
 */
sealed class ApplicationCommandDeclaration(
    val name: String,
    val nameLocalizations: Map<Locale, String>? = null
)

class SlashCommandDeclaration(
    name: String,
    nameLocalizations: Map<Locale, String>? = null,
    val description: String,
    val descriptionLocalizations: Map<Locale, String>? = null,
    val executor: SlashCommandExecutorDeclaration? = null,
    val options: List<InteraKTionsCommandOption<*>>?,
    val defaultMemberPermissions: Permissions?,
    val dmPermission: Boolean?,
    val subcommands: List<SlashCommandDeclaration>,
    val subcommandGroups: List<SlashCommandGroupDeclaration>
) : ApplicationCommandDeclaration(name, nameLocalizations)

class SlashCommandGroupDeclaration(
    name: String,
    nameLocalizations: Map<Locale, String>? = null,
    val description: String,
    val descriptionLocalizations: Map<Locale, String>? = null,
    val subcommands: List<SlashCommandDeclaration>
) : ApplicationCommandDeclaration(name, nameLocalizations)

class UserCommandDeclaration(
    name: String,
    nameLocalizations: Map<Locale, String>? = null,
    val defaultMemberPermissions: Permissions?,
    val dmPermission: Boolean?,
    val executor: UserCommandExecutorDeclaration // User/Message commands always requires an executor, that's why it is not nullable!
) : ApplicationCommandDeclaration(name, nameLocalizations)

class MessageCommandDeclaration(
    name: String,
    nameLocalizations: Map<Locale, String>? = null,
    val defaultMemberPermissions: Permissions?,
    val dmPermission: Boolean?,
    val executor: MessageCommandExecutorDeclaration // User/Message commands always requires an executor, that's why it is not nullable!
) : ApplicationCommandDeclaration(name, nameLocalizations)
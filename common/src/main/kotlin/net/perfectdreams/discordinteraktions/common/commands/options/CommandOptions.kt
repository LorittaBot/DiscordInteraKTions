package net.perfectdreams.discordinteraktions.common.commands.options

import dev.kord.common.Locale
import dev.kord.common.entity.DiscordAttachment
import net.perfectdreams.discordinteraktions.common.entities.Channel
import net.perfectdreams.discordinteraktions.common.entities.Role
import net.perfectdreams.discordinteraktions.common.entities.User
import net.perfectdreams.discordinteraktions.common.autocomplete.AutocompleteExecutorDeclaration

sealed class CommandOption<T>(
    val name: String,
    val nameLocalizations: Map<Locale, String>?,
    val description: String,
    val descriptionLocalizations: Map<Locale, String>?
)

interface NullableCommandOption

sealed class ChoiceableCommandOption<T, ChoiceableType>(
    name: String,
    nameLocalizations: Map<Locale, String>?,
    description: String,
    descriptionLocalizations: Map<Locale, String>?,
    val choices: List<CommandChoice<ChoiceableType>>,
    val autoCompleteExecutorDeclaration: AutocompleteExecutorDeclaration<ChoiceableType>?
) : CommandOption<T>(name, nameLocalizations, description, descriptionLocalizations)

// ===[ STRING ]===
class StringCommandOption(
    name: String,
    nameLocalizations: Map<Locale, String>?,
    description: String,
    descriptionLocalizations: Map<Locale, String>?,
    choices: List<CommandChoice<String>>,
    autoCompleteExecutorDeclaration: AutocompleteExecutorDeclaration<String>?
) : ChoiceableCommandOption<String, String>(name, nameLocalizations, description, descriptionLocalizations, choices, autoCompleteExecutorDeclaration)

class NullableStringCommandOption(
    name: String,
    nameLocalizations: Map<Locale, String>?,
    description: String,
    descriptionLocalizations: Map<Locale, String>?,
    choices: List<CommandChoice<String>>,
    autoCompleteExecutorDeclaration: AutocompleteExecutorDeclaration<String>?
) : ChoiceableCommandOption<String?, String>(name, nameLocalizations, description, descriptionLocalizations, choices, autoCompleteExecutorDeclaration), NullableCommandOption

// ===[ INTEGER ]===
class IntegerCommandOption(
    name: String,
    nameLocalizations: Map<Locale, String>?,
    description: String,
    descriptionLocalizations: Map<Locale, String>?,
    choices: List<CommandChoice<Long>>,
    autoCompleteExecutorDeclaration: AutocompleteExecutorDeclaration<Long>?
) : ChoiceableCommandOption<Long, Long>(name, nameLocalizations, description, descriptionLocalizations, choices, autoCompleteExecutorDeclaration)

class NullableIntegerCommandOption(
    name: String,
    nameLocalizations: Map<Locale, String>?,
    description: String,
    descriptionLocalizations: Map<Locale, String>?,
    choices: List<CommandChoice<Long>>,
    autoCompleteExecutorDeclaration: AutocompleteExecutorDeclaration<Long>?
) : ChoiceableCommandOption<Long?, Long>(name, nameLocalizations, description, descriptionLocalizations, choices, autoCompleteExecutorDeclaration), NullableCommandOption

// ===[ NUMBER ]===
class NumberCommandOption(
    name: String,
    nameLocalizations: Map<Locale, String>?,
    description: String,
    descriptionLocalizations: Map<Locale, String>?,
    choices: List<CommandChoice<Double>>,
    autoCompleteExecutorDeclaration: AutocompleteExecutorDeclaration<Double>?
) : ChoiceableCommandOption<Double, Double>(name, nameLocalizations, description, descriptionLocalizations, choices, autoCompleteExecutorDeclaration)

class NullableNumberCommandOption(
    name: String,
    nameLocalizations: Map<Locale, String>?,
    description: String,
    descriptionLocalizations: Map<Locale, String>?,
    choices: List<CommandChoice<Double>>,
    autoCompleteExecutorDeclaration: AutocompleteExecutorDeclaration<Double>?
) : ChoiceableCommandOption<Double?, Double>(name, nameLocalizations, description, descriptionLocalizations, choices, autoCompleteExecutorDeclaration), NullableCommandOption

// ===[ BOOLEAN ]===
class BooleanCommandOption(name: String, nameLocalizations: Map<Locale, String>?, description: String, descriptionLocalizations: Map<Locale, String>?) : CommandOption<Boolean>(name, nameLocalizations, description, descriptionLocalizations)
class NullableBooleanCommandOption(name: String, nameLocalizations: Map<Locale, String>?, description: String, descriptionLocalizations: Map<Locale, String>?) : CommandOption<Boolean?>(name, nameLocalizations, description, descriptionLocalizations), NullableCommandOption

// ===[ USER ]===
class UserCommandOption(name: String, nameLocalizations: Map<Locale, String>?, description: String, descriptionLocalizations: Map<Locale, String>?) : CommandOption<User>(name, nameLocalizations, description, descriptionLocalizations)
class NullableUserCommandOption(name: String, nameLocalizations: Map<Locale, String>?, description: String, descriptionLocalizations: Map<Locale, String>?) : CommandOption<User?>(name, nameLocalizations, description, descriptionLocalizations), NullableCommandOption

// ===[ CHANNEL ]===
class ChannelCommandOption(name: String, nameLocalizations: Map<Locale, String>?, description: String, descriptionLocalizations: Map<Locale, String>?) : CommandOption<Channel>(name, nameLocalizations, description, descriptionLocalizations)
class NullableChannelCommandOption(name: String, nameLocalizations: Map<Locale, String>?, description: String, descriptionLocalizations: Map<Locale, String>?) : CommandOption<Channel?>(name, nameLocalizations, description, descriptionLocalizations), NullableCommandOption

// ===[ ROLE ]===
class RoleCommandOption(name: String, nameLocalizations: Map<Locale, String>?, description: String, descriptionLocalizations: Map<Locale, String>?) : CommandOption<Role>(name, nameLocalizations, description, descriptionLocalizations)
class NullableRoleCommandOption(name: String, nameLocalizations: Map<Locale, String>?, description: String, descriptionLocalizations: Map<Locale, String>?) : CommandOption<Role?>(name, nameLocalizations, description, descriptionLocalizations), NullableCommandOption

// ===[ ATTACHMENT ]===
class AttachmentCommandOption(name: String, nameLocalizations: Map<Locale, String>?, description: String, descriptionLocalizations: Map<Locale, String>?) : CommandOption<DiscordAttachment>(name, nameLocalizations, description, descriptionLocalizations)
class NullableAttachmentCommandOption(name: String, nameLocalizations: Map<Locale, String>?, description: String, descriptionLocalizations: Map<Locale, String>?) : CommandOption<DiscordAttachment?>(name, nameLocalizations, description, descriptionLocalizations), NullableCommandOption
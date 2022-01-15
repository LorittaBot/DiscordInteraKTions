package net.perfectdreams.discordinteraktions.common.commands.options

import net.perfectdreams.discordinteraktions.common.entities.Channel
import net.perfectdreams.discordinteraktions.common.entities.Role
import net.perfectdreams.discordinteraktions.common.entities.User
import net.perfectdreams.discordinteraktions.common.autocomplete.AutocompleteExecutorDeclaration

sealed class CommandOption<T>(
    val name: String,
    val description: String
)

interface NullableCommandOption

sealed class ChoiceableCommandOption<T, ChoiceableType>(
    name: String,
    description: String,
    val choices: List<CommandChoice<ChoiceableType>>,
    val autoCompleteExecutorDeclaration: AutocompleteExecutorDeclaration<ChoiceableType>?
) : CommandOption<T>(name, description)

// ===[ STRING ]===
class StringCommandOption(
    name: String,
    description: String,
    choices: List<CommandChoice<String>>,
    autoCompleteExecutorDeclaration: AutocompleteExecutorDeclaration<String>?
) : ChoiceableCommandOption<String, String>(name, description, choices, autoCompleteExecutorDeclaration)

class NullableStringCommandOption(
    name: String,
    description: String,
    choices: List<CommandChoice<String>>,
    autoCompleteExecutorDeclaration: AutocompleteExecutorDeclaration<String>?
) : ChoiceableCommandOption<String?, String>(name, description, choices, autoCompleteExecutorDeclaration), NullableCommandOption

// ===[ INTEGER ]===
class IntegerCommandOption(
    name: String,
    description: String,
    choices: List<CommandChoice<Long>>,
    autoCompleteExecutorDeclaration: AutocompleteExecutorDeclaration<Long>?
) : ChoiceableCommandOption<Long, Long>(name, description, choices, autoCompleteExecutorDeclaration)

class NullableIntegerCommandOption(
    name: String,
    description: String,
    choices: List<CommandChoice<Long>>,
    autoCompleteExecutorDeclaration: AutocompleteExecutorDeclaration<Long>?
) : ChoiceableCommandOption<Long?, Long>(name, description, choices, autoCompleteExecutorDeclaration), NullableCommandOption

// ===[ NUMBER ]===
class NumberCommandOption(
    name: String,
    description: String,
    choices: List<CommandChoice<Double>>,
    autoCompleteExecutorDeclaration: AutocompleteExecutorDeclaration<Double>?
) : ChoiceableCommandOption<Double, Double>(name, description, choices, autoCompleteExecutorDeclaration)

class NullableNumberCommandOption(
    name: String,
    description: String,
    choices: List<CommandChoice<Double>>,
    autoCompleteExecutorDeclaration: AutocompleteExecutorDeclaration<Double>?
) : ChoiceableCommandOption<Double?, Double>(name, description, choices, autoCompleteExecutorDeclaration), NullableCommandOption

// ===[ BOOLEAN ]===
class BooleanCommandOption(name: String, description: String) : CommandOption<Boolean>(name, description)
class NullableBooleanCommandOption(name: String, description: String) : CommandOption<Boolean?>(name, description), NullableCommandOption

// ===[ USER ]===
class UserCommandOption(name: String, description: String) : CommandOption<User>(name, description)
class NullableUserCommandOption(name: String, description: String) : CommandOption<User?>(name, description), NullableCommandOption

// ===[ CHANNEL ]===
class ChannelCommandOption(name: String, description: String) : CommandOption<Channel>(name, description)
class NullableChannelCommandOption(name: String, description: String) : CommandOption<Channel?>(name, description), NullableCommandOption

// ===[ ROLE ]===
class RoleCommandOption(name: String, description: String) : CommandOption<Role>(name, description)
class NullableRoleCommandOption(name: String, description: String) : CommandOption<Role?>(name, description), NullableCommandOption
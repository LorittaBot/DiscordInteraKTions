package net.perfectdreams.discordinteraktions.common.commands.options

import dev.kord.common.Locale
import dev.kord.common.entity.DiscordAttachment
import net.perfectdreams.discordinteraktions.common.entities.Channel
import net.perfectdreams.discordinteraktions.common.entities.Role
import net.perfectdreams.discordinteraktions.common.entities.User
import net.perfectdreams.discordinteraktions.common.autocomplete.AutocompleteExecutorDeclaration

sealed class CommandOptionBuilder<T, ChoiceableType>(
    val name: String,
    val description: String,
) {
    var nameLocalizations: Map<Locale, String>? = null
    var descriptionLocalizations: Map<Locale, String>? = null

    fun nameLocalizations(map: Map<Locale, String>): CommandOptionBuilder<T, ChoiceableType> {
        this.nameLocalizations = map
        return this
    }

    fun descriptionLocalizations(map: Map<Locale, String>): CommandOptionBuilder<T, ChoiceableType> {
        this.descriptionLocalizations = map
        return this
    }

    abstract fun build(): CommandOption<T>
}

sealed class ChoiceableCommandOptionBuilder<T, ChoiceableType>(
    name: String,
    description: String,
) : CommandOptionBuilder<T, ChoiceableType>(name, description) {
    val choices: MutableList<CommandChoice<ChoiceableType>> = mutableListOf()
    var autocompleteExecutorDeclaration: AutocompleteExecutorDeclaration<ChoiceableType>? = null

    fun choice(value: ChoiceableType, name: String, nameLocalizations: Map<Locale, String>? = null): ChoiceableCommandOptionBuilder<T, ChoiceableType> {
        if (this.autocompleteExecutorDeclaration != null)
            error("You can't use pre-defined choices with an autocomplete executor set!")

        choices.add(buildCommandChoice(value, name, nameLocalizations))

        return this
    }

    fun autocomplete(autocompleteExecutorDeclaration: AutocompleteExecutorDeclaration<ChoiceableType>): ChoiceableCommandOptionBuilder<T, ChoiceableType> {
        if (this.choices.isNotEmpty())
            error("You can't use an autocomplete executor with pre-defined choices set!")

        this.autocompleteExecutorDeclaration = autocompleteExecutorDeclaration

        return this
    }

    abstract fun buildCommandChoice(value: ChoiceableType, name: String, map: Map<Locale, String>? = null): CommandChoice<ChoiceableType>
}

// ===[ STRING ]===
class StringCommandOptionBuilder(name: String, description: String) : ChoiceableCommandOptionBuilder<String, String>(name, description) {
    override fun buildCommandChoice(value: String, name: String, map: Map<Locale, String>?) = StringCommandChoice(name, value, map)

    override fun build() = StringCommandOption(
        name,
        description,
        choices,
        autocompleteExecutorDeclaration
    )
}

class NullableStringCommandOptionBuilder(name: String, description: String) : ChoiceableCommandOptionBuilder<String?, String>(name, description) {
    override fun buildCommandChoice(value: String, name: String, map: Map<Locale, String>?) = StringCommandChoice(name, value, map)

    override fun build() = NullableStringCommandOption(
        name,
        description,
        choices,
        autocompleteExecutorDeclaration
    )
}

// ===[ INTEGER ]===
class IntegerCommandOptionBuilder(name: String, description: String) : ChoiceableCommandOptionBuilder<Long, Long>(name, description) {
    override fun buildCommandChoice(value: Long, name: String, map: Map<Locale, String>?) = IntegerCommandChoice(name, value, map)

    override fun build() = IntegerCommandOption(
        name,
        description,
        choices,
        autocompleteExecutorDeclaration
    )
}

class NullableIntegerCommandOptionBuilder(name: String, description: String) : ChoiceableCommandOptionBuilder<Long?, Long>(name, description) {
    override fun buildCommandChoice(value: Long, name: String, map: Map<Locale, String>?) = IntegerCommandChoice(name, value, map)

    override fun build() = NullableIntegerCommandOption(
        name,
        description,
        choices,
        autocompleteExecutorDeclaration
    )
}

// ===[ NUMBER ]===
class NumberCommandOptionBuilder(name: String, description: String) : ChoiceableCommandOptionBuilder<Double, Double>(name, description) {
    override fun buildCommandChoice(value: Double, name: String, map: Map<Locale, String>?) = NumberCommandChoice(name, value, map)

    override fun build() = NumberCommandOption(
        name,
        description,
        choices,
        autocompleteExecutorDeclaration
    )
}

class NullableNumberCommandOptionBuilder(name: String, description: String) : ChoiceableCommandOptionBuilder<Double?, Double>(name, description) {
    override fun buildCommandChoice(value: Double, name: String, map: Map<Locale, String>?) = NumberCommandChoice(name, value, map)

    override fun build() = NullableNumberCommandOption(
        name,
        description,
        choices,
        autocompleteExecutorDeclaration
    )
}

// ===[ BOOLEAN ]===
class BooleanCommandOptionBuilder(name: String, description: String) : CommandOptionBuilder<Boolean, Boolean>(name, description) {
    override fun build() = BooleanCommandOption(name, description)
}

class NullableBooleanCommandOptionBuilder(name: String, description: String) : CommandOptionBuilder<Boolean?, Boolean>(name, description) {
    override fun build() = NullableBooleanCommandOption(name, description)
}

// ===[ USER ]===
class UserCommandOptionBuilder(name: String, description: String) : CommandOptionBuilder<User, User>(name, description) {
    override fun build() = UserCommandOption(name, description)
}

class NullableUserCommandOptionBuilder(name: String, description: String) : CommandOptionBuilder<User?, User>(name, description) {
    override fun build() = NullableUserCommandOption(name, description)
}

// ===[ CHANNEL ]===
class ChannelCommandOptionBuilder(name: String, description: String) : CommandOptionBuilder<Channel, Channel>(name, description) {
    override fun build() = ChannelCommandOption(name, description)
}

class NullableChannelCommandOptionBuilder(name: String, description: String) : CommandOptionBuilder<Channel?, Channel>(name, description) {
    override fun build() = NullableChannelCommandOption(name, description)
}

// ===[ ROLE ]===
class RoleCommandOptionBuilder(name: String, description: String) : CommandOptionBuilder<Role, Role>(name, description) {
    override fun build() = RoleCommandOption(name, description)
}

class NullableRoleCommandOptionBuilder(name: String, description: String) : CommandOptionBuilder<Role?, Role>(name, description) {
    override fun build() = NullableRoleCommandOption(name, description)
}

// ===[ ATTACHMENT ]===
class AttachmentCommandOptionBuilder(name: String, description: String) : CommandOptionBuilder<DiscordAttachment, DiscordAttachment>(name, description) {
    override fun build() = AttachmentCommandOption(name, description)
}

class NullableAttachmentCommandOptionBuilder(name: String, description: String) : CommandOptionBuilder<DiscordAttachment?, DiscordAttachment>(name, description) {
    override fun build() = NullableAttachmentCommandOption(name, description)
}
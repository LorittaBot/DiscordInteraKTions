package net.perfectdreams.discordinteraktions.common.commands.options

import dev.kord.common.Locale
import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.DiscordAttachment
import net.perfectdreams.discordinteraktions.common.autocomplete.AutocompleteExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.entities.Channel
import net.perfectdreams.discordinteraktions.common.entities.Mentionable
import net.perfectdreams.discordinteraktions.common.entities.Role
import net.perfectdreams.discordinteraktions.common.entities.User
import net.perfectdreams.discordinteraktions.common.stringhandlers.RawStringData
import net.perfectdreams.discordinteraktions.common.stringhandlers.StringData
import net.perfectdreams.discordinteraktions.common.stringhandlers.StringDataHandlers

abstract class CommandOptionBuilder<T, ChoiceableType>(
    // We don't allow providing a StringProvider here because Discord InteraKTions uses the "name" to match arguments to options
    val name: String,
    val description: StringData<*>,
    val required: Boolean
) {
    var nameLocalizations: Map<Locale, StringData<*>>? = null
    var descriptionLocalizations: Map<Locale, StringData<*>>? = null

    fun nameLocalizations(localizationsMap: Map<Locale, String>) =
        nameLocalizations(
            localizationsMap.entries.associate { it.key to RawStringData(it.value) }
        )

    @JvmName("nameLocalizationsFromProvider")
    fun nameLocalizations(localizationsMap: Map<Locale, StringData<*>>) {
        this.nameLocalizations = localizationsMap
    }

    fun descriptionLocalizations(localizationsMap: Map<Locale, String>) =
        descriptionLocalizations(
            localizationsMap.entries.associate { it.key to RawStringData(it.value) }
        )

    @JvmName("descriptionLocalizationsFromProvider")
    fun descriptionLocalizations(localizationsMap: Map<Locale, StringData<*>>) {
        this.descriptionLocalizations = localizationsMap
    }

    abstract fun build(handlers: StringDataHandlers): InteraKTionsCommandOption<ChoiceableType>
}

abstract class ChoiceableCommandOptionBuilder<T, ChoiceableType>(
    name: String,
    description: StringData<*>,
    required: Boolean
) : CommandOptionBuilder<T, ChoiceableType>(name, description, required) {
    var choices: MutableList<CommandChoiceBuilder<ChoiceableType>>? = mutableListOf()
    var autocomplete: AutocompleteExecutorDeclaration<ChoiceableType>? = null

    fun choice(name: String, value: ChoiceableType, block: CommandChoiceBuilder<ChoiceableType>.() -> (Unit) = {})
            = choice(RawStringData(name), value, block)

    fun choice(name: StringData<*>, value: ChoiceableType, block: CommandChoiceBuilder<ChoiceableType>.() -> (Unit) = {}) {
        require(autocomplete == null) {
            "You can't use pre-defined choices with an autocomplete executor set!"
        }

        val builder = CommandChoiceBuilder(name, value).apply(block)

        if (choices == null)
            choices = mutableListOf()
        choices?.add(builder)
    }

    fun autocomplete(declaration: AutocompleteExecutorDeclaration<ChoiceableType>) {
        require(choices?.isNotEmpty() == false) {
            "You can't use autocomplete with pre-defined choices!"
        }

        autocomplete = declaration
    }
}

// ===[ STRING ]===
abstract class StringCommandOptionBuilderBase<T>(
    name: String,
    description: StringData<*>,
    required: Boolean
) : ChoiceableCommandOptionBuilder<T, String>(name, description, required) {
    var minLength: Int? = null
    var maxLength: Int? = null

    override fun build(handlers: StringDataHandlers) = StringCommandOption(
        name,
        handlers.provide(description),
        nameLocalizations?.entries?.associate { it.key to handlers.provide(it.value) },
        descriptionLocalizations?.entries?.associate { it.key to handlers.provide(it.value) },
        required,
        choices?.map { it.build(handlers) },
        minLength,
        maxLength,
        autocomplete != null
    )
}

class StringCommandOptionBuilder(
    name: String,
    description: StringData<*>
) : StringCommandOptionBuilderBase<String>(name, description, true)

class NullableStringCommandOptionBuilder(
    name: String,
    description: StringData<*>
) : StringCommandOptionBuilderBase<String?>(name, description, false)

// ===[ INTEGER ]===
abstract class IntegerCommandOptionBuilderBase<T>(
    name: String,
    description: StringData<*>,
    required: Boolean
) : ChoiceableCommandOptionBuilder<T, Long>(name, description, required) {
    var minValue: Long? = null
    var maxValue: Long? = null
    var range: LongRange
        get() = error("This is a settable property only")
        set(value) {
            minValue = value.first
            maxValue = value.last
        }

    override fun build(handlers: StringDataHandlers) = IntegerCommandOption(
        name,
        handlers.provide(description),
        nameLocalizations?.entries?.associate { it.key to handlers.provide(it.value) },
        descriptionLocalizations?.entries?.associate { it.key to handlers.provide(it.value) },
        required,
        choices?.map { it.build(handlers) },
        minValue,
        maxValue,
        autocomplete != null
    )
}

class IntegerCommandOptionBuilder(
    name: String,
    description: StringData<*>
) : IntegerCommandOptionBuilderBase<Long>(name, description, true)

class NullableIntegerCommandOptionBuilder(
    name: String,
    description: StringData<*>
) : IntegerCommandOptionBuilderBase<Long?>(name, description, false)

// ===[ NUMBER ]===
abstract class NumberCommandOptionBuilderBase<T>(
    name: String,
    description: StringData<*>,
    required: Boolean
) : ChoiceableCommandOptionBuilder<T, Double>(name, description, required) {
    var minValue: Double? = null
    var maxValue: Double? = null
    var range: ClosedFloatingPointRange<Double>
        get() = error("This is a settable property only")
        set(value) {
            minValue = value.start
            maxValue = value.endInclusive
        }

    override fun build(handlers: StringDataHandlers) = NumberCommandOption(
        name,
        handlers.provide(description),
        nameLocalizations?.entries?.associate { it.key to handlers.provide(it.value) },
        descriptionLocalizations?.entries?.associate { it.key to handlers.provide(it.value) },
        required,
        choices?.map { it.build(handlers) },
        minValue,
        maxValue,
        autocomplete != null
    )
}

class NumberCommandOptionBuilder(
    name: String,
    description: StringData<*>
) : NumberCommandOptionBuilderBase<Double>(name, description, true)

class NullableNumberCommandOptionBuilder(
    name: String,
    description: StringData<*>
) : NumberCommandOptionBuilderBase<Double?>(name, description, false)

// ===[ BOOLEAN ]===
abstract class BooleanCommandOptionBuilderBase<T>(
    name: String,
    description: StringData<*>,
    required: Boolean
) : CommandOptionBuilder<T, Boolean>(name, description, required) {
    override fun build(handlers: StringDataHandlers) = BooleanCommandOption(
        name,
        handlers.provide(description),
        nameLocalizations?.entries?.associate { it.key to handlers.provide(it.value) },
        descriptionLocalizations?.entries?.associate { it.key to handlers.provide(it.value) },
        required
    )
}

class BooleanCommandOptionBuilder(
    name: String,
    description: StringData<*>
) : BooleanCommandOptionBuilderBase<Boolean>(name, description, true)

class NullableBooleanCommandOptionBuilder(
    name: String,
    description: StringData<*>
) : BooleanCommandOptionBuilderBase<Boolean?>(name, description, false)

// ===[ USER ]===
abstract class UserCommandOptionBuilderBase<T>(
    name: String,
    description: StringData<*>,
    required: Boolean
) : CommandOptionBuilder<T, User>(name, description, required) {
    override fun build(handlers: StringDataHandlers) = UserCommandOption(
        name,
        handlers.provide(description),
        nameLocalizations?.entries?.associate { it.key to handlers.provide(it.value) },
        descriptionLocalizations?.entries?.associate { it.key to handlers.provide(it.value) },
        required
    )
}

class UserCommandOptionBuilder(
    name: String,
    description: StringData<*>
) : UserCommandOptionBuilderBase<User>(name, description, true)

class NullableUserCommandOptionBuilder(
    name: String,
    description: StringData<*>
) : UserCommandOptionBuilderBase<User?>(name, description, false)

// ===[ ROLE ]===
abstract class RoleCommandOptionBuilderBase<T>(
    name: String,
    description: StringData<*>,
    required: Boolean
) : CommandOptionBuilder<T, Role>(name, description, required) {
    override fun build(handlers: StringDataHandlers) = RoleCommandOption(
        name,
        handlers.provide(description),
        nameLocalizations?.entries?.associate { it.key to handlers.provide(it.value) },
        descriptionLocalizations?.entries?.associate { it.key to handlers.provide(it.value) },
        required
    )
}

class RoleCommandOptionBuilder(
    name: String,
    description: StringData<*>
) : RoleCommandOptionBuilderBase<Role>(name, description, true)

class NullableRoleCommandOptionBuilder(
    name: String,
    description: StringData<*>
) : RoleCommandOptionBuilderBase<Role?>(name, description, false)

// ===[ CHANNEL ]===
abstract class ChannelCommandOptionBuilderBase<T>(
    name: String,
    description: StringData<*>,
    required: Boolean
) : CommandOptionBuilder<T, Channel>(name, description, required) {
    var channelTypes: List<ChannelType>? = null

    override fun build(handlers: StringDataHandlers) = ChannelCommandOption(
        name,
        handlers.provide(description),
        nameLocalizations?.entries?.associate { it.key to handlers.provide(it.value) },
        descriptionLocalizations?.entries?.associate { it.key to handlers.provide(it.value) },
        required,
        channelTypes
    )
}

class ChannelCommandOptionBuilder(
    name: String,
    description: StringData<*>
) : ChannelCommandOptionBuilderBase<Role>(name, description, true)

class NullableChannelCommandOptionBuilder(
    name: String,
    description: StringData<*>
) : ChannelCommandOptionBuilderBase<Role?>(name, description, false)

// ===[ MENTIONABLE ]===
abstract class MentionableCommandOptionBuilderBase<T>(
    name: String,
    description: StringData<*>,
    required: Boolean
) : CommandOptionBuilder<T, Mentionable>(name, description, required) {
    override fun build(handlers: StringDataHandlers) = MentionableCommandOption(
        name,
        handlers.provide(description),
        nameLocalizations?.entries?.associate { it.key to handlers.provide(it.value) },
        descriptionLocalizations?.entries?.associate { it.key to handlers.provide(it.value) },
        required
    )
}

class MentionableCommandOptionBuilder(
    name: String,
    description: StringData<*>
) : MentionableCommandOptionBuilderBase<Mentionable>(name, description, true)

class NullableMentionableCommandOptionBuilder(
    name: String,
    description: StringData<*>
) : MentionableCommandOptionBuilderBase<Mentionable?>(name, description, false)

// ===[ ATTACHMENT ]===
abstract class AttachmentCommandOptionBuilderBase<T>(
    name: String,
    description: StringData<*>,
    required: Boolean
) : CommandOptionBuilder<T, DiscordAttachment>(name, description, required) {
    override fun build(handlers: StringDataHandlers) = AttachmentCommandOption(
        name,
        handlers.provide(description),
        nameLocalizations?.entries?.associate { it.key to handlers.provide(it.value) },
        descriptionLocalizations?.entries?.associate { it.key to handlers.provide(it.value) },
        required
    )
}

class AttachmentCommandOptionBuilder(
    name: String,
    description: StringData<*>
) : AttachmentCommandOptionBuilderBase<DiscordAttachment>(name, description, true)

class NullableAttachmentCommandOptionBuilder(
    name: String,
    description: StringData<*>
) : AttachmentCommandOptionBuilderBase<DiscordAttachment?>(name, description, false)
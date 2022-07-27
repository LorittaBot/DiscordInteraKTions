package net.perfectdreams.discordinteraktions.common.commands.options

import dev.kord.common.Locale
import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.DiscordAttachment
import net.perfectdreams.discordinteraktions.common.autocomplete.AutocompleteHandler
import net.perfectdreams.discordinteraktions.common.entities.Channel
import net.perfectdreams.discordinteraktions.common.entities.Mentionable
import net.perfectdreams.discordinteraktions.common.entities.Role
import net.perfectdreams.discordinteraktions.common.entities.User

abstract class CommandOptionBuilder<T, ChoiceableType>(
    val name: String,
    val description: String,
    val required: Boolean
) {
    var nameLocalizations: Map<Locale, String>? = null
    var descriptionLocalizations: Map<Locale, String>? = null

    abstract fun build(): InteraKTionsCommandOption<ChoiceableType>
}

abstract class ChoiceableCommandOptionBuilder<T, ChoiceableType>(
    name: String,
    description: String,
    required: Boolean
) : CommandOptionBuilder<T, ChoiceableType>(name, description, required) {
    var choices: MutableList<CommandChoiceBuilder<ChoiceableType>>? = mutableListOf()
    var autocompleteExecutor: AutocompleteHandler<ChoiceableType>? = null

    fun choice(name: String, value: ChoiceableType, block: CommandChoiceBuilder<ChoiceableType>.() -> (Unit) = {}) {
        require(autocompleteExecutor == null) {
            "You can't use pre-defined choices with an autocomplete executor set!"
        }

        val builder = CommandChoiceBuilder(name, value).apply(block)

        if (choices == null)
            choices = mutableListOf()
        choices?.add(builder)
    }

    fun autocomplete(handler: AutocompleteHandler<ChoiceableType>) {
        require(choices?.isNotEmpty() == false) {
            "You can't use autocomplete with pre-defined choices!"
        }

        autocompleteExecutor = handler
    }
}

// ===[ STRING ]===
abstract class StringCommandOptionBuilderBase<T>(
    name: String,
    description: String,
    required: Boolean
) : ChoiceableCommandOptionBuilder<T, String>(name, description, required) {
    var minLength: Int? = null
    var maxLength: Int? = null
    var allowedLength: IntRange
        get() = error("This is a settable property only")
        set(value) {
            minLength = value.first
            maxLength = value.last
        }

    override fun build() = StringCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations,
        required,
        choices?.map { it.build() },
        minLength,
        maxLength,
        autocompleteExecutor
    )
}

class StringCommandOptionBuilder(
    name: String,
    description: String
) : StringCommandOptionBuilderBase<String>(name, description, true)

class NullableStringCommandOptionBuilder(
    name: String,
    description: String
) : StringCommandOptionBuilderBase<String?>(name, description, false)

// ===[ INTEGER ]===
abstract class IntegerCommandOptionBuilderBase<T>(
    name: String,
    description: String,
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

    override fun build() = IntegerCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations,
        required,
        choices?.map { it.build() },
        minValue,
        maxValue,
        autocompleteExecutor
    )
}

class IntegerCommandOptionBuilder(
    name: String,
    description: String
) : IntegerCommandOptionBuilderBase<Long>(name, description, true)

class NullableIntegerCommandOptionBuilder(
    name: String,
    description: String
) : IntegerCommandOptionBuilderBase<Long?>(name, description, false)

// ===[ NUMBER ]===
abstract class NumberCommandOptionBuilderBase<T>(
    name: String,
    description: String,
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

    override fun build() = NumberCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations,
        required,
        choices?.map { it.build() },
        minValue,
        maxValue,
        autocompleteExecutor
    )
}

class NumberCommandOptionBuilder(
    name: String,
    description: String
) : NumberCommandOptionBuilderBase<Double>(name, description, true)

class NullableNumberCommandOptionBuilder(
    name: String,
    description: String
) : NumberCommandOptionBuilderBase<Double?>(name, description, false)

// ===[ BOOLEAN ]===
abstract class BooleanCommandOptionBuilderBase<T>(
    name: String,
    description: String,
    required: Boolean
) : CommandOptionBuilder<T, Boolean>(name, description, required) {
    override fun build() = BooleanCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations,
        required
    )
}

class BooleanCommandOptionBuilder(
    name: String,
    description: String
) : BooleanCommandOptionBuilderBase<Boolean>(name, description, true)

class NullableBooleanCommandOptionBuilder(
    name: String,
    description: String
) : BooleanCommandOptionBuilderBase<Boolean?>(name, description, false)

// ===[ USER ]===
abstract class UserCommandOptionBuilderBase<T>(
    name: String,
    description: String,
    required: Boolean
) : CommandOptionBuilder<T, User>(name, description, required) {
    override fun build() = UserCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations,
        required
    )
}

class UserCommandOptionBuilder(
    name: String,
    description: String
) : UserCommandOptionBuilderBase<User>(name, description, true)

class NullableUserCommandOptionBuilder(
    name: String,
    description: String
) : UserCommandOptionBuilderBase<User?>(name, description, false)

// ===[ ROLE ]===
abstract class RoleCommandOptionBuilderBase<T>(
    name: String,
    description: String,
    required: Boolean
) : CommandOptionBuilder<T, Role>(name, description, required) {
    override fun build() = RoleCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations,
        required
    )
}

class RoleCommandOptionBuilder(
    name: String,
    description: String
) : RoleCommandOptionBuilderBase<Role>(name, description, true)

class NullableRoleCommandOptionBuilder(
    name: String,
    description: String
) : RoleCommandOptionBuilderBase<Role?>(name, description, false)

// ===[ CHANNEL ]===
abstract class ChannelCommandOptionBuilderBase<T>(
    name: String,
    description: String,
    required: Boolean
) : CommandOptionBuilder<T, Channel>(name, description, required) {
    var channelTypes: List<ChannelType>? = null

    override fun build() = ChannelCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations,
        required,
        channelTypes
    )
}

class ChannelCommandOptionBuilder(
    name: String,
    description: String
) : ChannelCommandOptionBuilderBase<Role>(name, description, true)

class NullableChannelCommandOptionBuilder(
    name: String,
    description: String
) : ChannelCommandOptionBuilderBase<Role?>(name, description, false)

// ===[ MENTIONABLE ]===
abstract class MentionableCommandOptionBuilderBase<T>(
    name: String,
    description: String,
    required: Boolean
) : CommandOptionBuilder<T, Mentionable>(name, description, required) {
    override fun build() = MentionableCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations,
        required
    )
}

class MentionableCommandOptionBuilder(
    name: String,
    description: String
) : MentionableCommandOptionBuilderBase<Mentionable>(name, description, true)

class NullableMentionableCommandOptionBuilder(
    name: String,
    description: String
) : MentionableCommandOptionBuilderBase<Mentionable?>(name, description, false)

// ===[ ATTACHMENT ]===
abstract class AttachmentCommandOptionBuilderBase<T>(
    name: String,
    description: String,
    required: Boolean
) : CommandOptionBuilder<T, DiscordAttachment>(name, description, required) {
    override fun build() = AttachmentCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations,
        required
    )
}

class AttachmentCommandOptionBuilder(
    name: String,
    description: String
) : AttachmentCommandOptionBuilderBase<DiscordAttachment>(name, description, true)

class NullableAttachmentCommandOptionBuilder(
    name: String,
    description: String
) : AttachmentCommandOptionBuilderBase<DiscordAttachment?>(name, description, false)
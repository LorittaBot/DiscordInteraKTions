package net.perfectdreams.discordinteraktions.common.commands.options

import dev.kord.common.Locale
import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.DiscordAttachment
import net.perfectdreams.discordinteraktions.common.autocomplete.AutocompleteHandler
import net.perfectdreams.discordinteraktions.common.entities.Channel
import net.perfectdreams.discordinteraktions.common.entities.Mentionable
import net.perfectdreams.discordinteraktions.common.entities.Role
import net.perfectdreams.discordinteraktions.common.entities.User

abstract class CommandOptionBuilder<T, ChoiceableType> {
    abstract val name: String

    /**
     * If the command option is required in the command.
     *
     * If [required] is true and the argument is not present, the command will fail.
     */
    abstract val required: Boolean

    abstract fun build(): InteraKTionsCommandOption<ChoiceableType>
}

abstract class DiscordCommandOptionBuilder<T, ChoiceableType> : CommandOptionBuilder<T, ChoiceableType>() {
    abstract val description: String

    var nameLocalizations: Map<Locale, String>? = null
    var descriptionLocalizations: Map<Locale, String>? = null
}

abstract class ChoiceableCommandOptionBuilder<T, ChoiceableType> : DiscordCommandOptionBuilder<T, ChoiceableType>() {
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
abstract class StringCommandOptionBuilderBase<T> : ChoiceableCommandOptionBuilder<T, String>() {
    var minLength: Int? = null
    var maxLength: Int? = null
    var allowedLength: IntRange
        get() = error("This is a settable property only")
        set(value) {
            minLength = value.first
            maxLength = value.last
        }

    override fun build(): StringCommandOption = DefaultStringCommandOption(
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
    override val name: String,
    override val description: String
) : StringCommandOptionBuilderBase<String>() {
    override val required = true
}

class NullableStringCommandOptionBuilder(
    override val name: String,
    override val description: String
) : StringCommandOptionBuilderBase<String?>() {
    override val required = true
}

// ===[ INTEGER ]===
abstract class IntegerCommandOptionBuilderBase<T> : ChoiceableCommandOptionBuilder<T, Long>() {
    var minValue: Long? = null
    var maxValue: Long? = null
    var range: LongRange
        get() = error("This is a settable property only")
        set(value) {
            minValue = value.first
            maxValue = value.last
        }

    override fun build(): IntegerCommandOption = DefaultIntegerCommandOption(
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
    override val name: String,
    override val description: String
) : IntegerCommandOptionBuilderBase<Long>() {
    override val required = true
}

class NullableIntegerCommandOptionBuilder(
    override val name: String,
    override val description: String
) : IntegerCommandOptionBuilderBase<Long?>() {
    override val required = false
}

// ===[ NUMBER ]===
abstract class NumberCommandOptionBuilderBase<T> : ChoiceableCommandOptionBuilder<T, Double>() {
    var minValue: Double? = null
    var maxValue: Double? = null
    var range: ClosedFloatingPointRange<Double>
        get() = error("This is a settable property only")
        set(value) {
            minValue = value.start
            maxValue = value.endInclusive
        }

    override fun build(): NumberCommandOption = DefaultNumberCommandOption(
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
    override val name: String,
    override val description: String
) : NumberCommandOptionBuilderBase<Double>() {
    override val required = true
}

class NullableNumberCommandOptionBuilder(
    override val name: String,
    override val description: String
) : NumberCommandOptionBuilderBase<Double?>() {
    override val required = false
}

// ===[ BOOLEAN ]===
abstract class BooleanCommandOptionBuilderBase<T> : DiscordCommandOptionBuilder<T, Boolean>() {
    override fun build(): BooleanCommandOption = DefaultBooleanCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations,
        required
    )
}

class BooleanCommandOptionBuilder(
    override val name: String,
    override val description: String
) : BooleanCommandOptionBuilderBase<Boolean>() {
    override val required = true
}

class NullableBooleanCommandOptionBuilder(
    override val name: String,
    override val description: String
) : BooleanCommandOptionBuilderBase<Boolean?>() {
    override val required = false
}

// ===[ USER ]===
abstract class UserCommandOptionBuilderBase<T> : DiscordCommandOptionBuilder<T, User>() {
    override fun build(): UserCommandOption = DefaultUserCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations,
        required
    )
}

class UserCommandOptionBuilder(
    override val name: String,
    override val description: String
) : UserCommandOptionBuilderBase<User>() {
    override val required = true
}

class NullableUserCommandOptionBuilder(
    override val name: String,
    override val description: String
) : UserCommandOptionBuilderBase<User?>() {
    override val required = false
}

// ===[ ROLE ]===
abstract class RoleCommandOptionBuilderBase<T> : DiscordCommandOptionBuilder<T, Role>() {
    override fun build(): RoleCommandOption = DefaultRoleCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations,
        required
    )
}

class RoleCommandOptionBuilder(
    override val name: String,
    override val description: String
) : RoleCommandOptionBuilderBase<Role>() {
    override val required = true
}

class NullableRoleCommandOptionBuilder(
    override val name: String,
    override val description: String
) : RoleCommandOptionBuilderBase<Role?>() {
    override val required = false
}

// ===[ CHANNEL ]===
abstract class ChannelCommandOptionBuilderBase<T> : DiscordCommandOptionBuilder<T, Channel>() {
    var channelTypes: List<ChannelType>? = null

    override fun build(): ChannelCommandOption = DefaultChannelCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations,
        required,
        channelTypes
    )
}

class ChannelCommandOptionBuilder(
    override val name: String,
    override val description: String
) : ChannelCommandOptionBuilderBase<Role>() {
    override val required = true
}

class NullableChannelCommandOptionBuilder(
    override val name: String,
    override val description: String
) : ChannelCommandOptionBuilderBase<Role?>() {
    override val required = false
}

// ===[ MENTIONABLE ]===
abstract class MentionableCommandOptionBuilderBase<T> : DiscordCommandOptionBuilder<T, Mentionable>() {
    override fun build(): MentionableCommandOption = DefaultMentionableCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations,
        required
    )
}

class MentionableCommandOptionBuilder(
    override val name: String,
    override val description: String
) : MentionableCommandOptionBuilderBase<Mentionable>() {
    override val required = true
}

class NullableMentionableCommandOptionBuilder(
    override val name: String,
    override val description: String
) : MentionableCommandOptionBuilderBase<Mentionable?>() {
    override val required = false
}

// ===[ ATTACHMENT ]===
abstract class AttachmentCommandOptionBuilderBase<T> : DiscordCommandOptionBuilder<T, DiscordAttachment>() {
    override fun build(): AttachmentCommandOption = DefaultAttachmentCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations,
        required
    )
}

class AttachmentCommandOptionBuilder(
    override val name: String,
    override val description: String
) : AttachmentCommandOptionBuilderBase<DiscordAttachment>() {
    override val required = true
}

class NullableAttachmentCommandOptionBuilder(
    override val name: String,
    override val description: String
) : AttachmentCommandOptionBuilderBase<DiscordAttachment?>() {
    override val required = false
}
package net.perfectdreams.discordinteraktions.common.commands.options

import net.perfectdreams.discordinteraktions.common.stringhandlers.RawStringData
import net.perfectdreams.discordinteraktions.common.stringhandlers.StringData

open class ApplicationCommandOptions {
    companion object {
        val NO_OPTIONS = object : ApplicationCommandOptions() {}
    }

    val optionBuilders = mutableListOf<CommandOptionBuilder<*, *>>()
    val references = mutableListOf<OptionReference<*>>()

    fun string(
        name: String,
        description: String,
        builder: StringCommandOptionBuilder.() -> (Unit) = {}
    ) = string(name, RawStringData(description), builder)

    fun string(
        name: String,
        description: StringData<*>,
        builder: StringCommandOptionBuilder.() -> (Unit) = {}
    ) = StringCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun optionalString(
        name: String,
        description: String,
        builder: NullableStringCommandOptionBuilder.() -> (Unit) = {}
    ) = optionalString(name, RawStringData(description), builder)

    fun optionalString(
        name: String,
        description: StringData<*>,
        builder: NullableStringCommandOptionBuilder.() -> (Unit) = {}
    ) = NullableStringCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun integer(
        name: String,
        description: String,
        builder: IntegerCommandOptionBuilder.() -> (Unit) = {}
    ) = integer(name, RawStringData(description), builder)

    fun integer(
        name: String,
        description: StringData<*>,
        builder: IntegerCommandOptionBuilder.() -> (Unit) = {}
    ) = IntegerCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun optionalInteger(
        name: String,
        description: String,
        builder: NullableIntegerCommandOptionBuilder.() -> (Unit) = {}
    ) = optionalInteger(name, RawStringData(description), builder)

    fun optionalInteger(
        name: String,
        description: StringData<*>,
        builder: NullableIntegerCommandOptionBuilder.() -> (Unit) = {}
    ) = NullableIntegerCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun number(
        name: String,
        description: String,
        builder: NumberCommandOptionBuilder.() -> (Unit) = {}
    ) = number(name, RawStringData(description), builder)

    fun number(
        name: String,
        description: StringData<*>,
        builder: NumberCommandOptionBuilder.() -> (Unit) = {}
    ) = NumberCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun optionalNumber(
        name: String,
        description: String,
        builder: NullableNumberCommandOptionBuilder.() -> (Unit) = {}
    ) = optionalNumber(name, RawStringData(description), builder)

    fun optionalNumber(
        name: String,
        description: StringData<*>,
        builder: NullableNumberCommandOptionBuilder.() -> (Unit) = {}
    ) = NullableNumberCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun boolean(
        name: String,
        description: String,
        builder: BooleanCommandOptionBuilder.() -> (Unit) = {}
    ) = boolean(name, RawStringData(description), builder)

    fun boolean(
        name: String,
        description: StringData<*>,
        builder: BooleanCommandOptionBuilder.() -> (Unit) = {}
    ) = BooleanCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun optionalBoolean(
        name: String,
        description: String,
        builder: NullableBooleanCommandOptionBuilder.() -> (Unit) = {}
    ) = optionalBoolean(name, RawStringData(description), builder)

    fun optionalBoolean(
        name: String,
        description: StringData<*>,
        builder: NullableBooleanCommandOptionBuilder.() -> (Unit) = {}
    ) = NullableBooleanCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun user(
        name: String,
        description: String,
        builder: UserCommandOptionBuilder.() -> (Unit) = {}
    ) = user(name, RawStringData(description), builder)

    fun user(
        name: String,
        description: StringData<*>,
        builder: UserCommandOptionBuilder.() -> (Unit) = {}
    ) = UserCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun optionalUser(
        name: String,
        description: String,
        builder: NullableUserCommandOptionBuilder.() -> (Unit) = {}
    ) = optionalUser(name, RawStringData(description), builder)

    fun optionalUser(
        name: String,
        description: StringData<*>,
        builder: NullableUserCommandOptionBuilder.() -> (Unit) = {}
    ) = NullableUserCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun role(
        name: String,
        description: String,
        builder: RoleCommandOptionBuilder.() -> (Unit) = {}
    ) = role(name, RawStringData(description), builder)

    fun role(
        name: String,
        description: StringData<*>,
        builder: RoleCommandOptionBuilder.() -> (Unit) = {}
    ) = RoleCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun optionalRole(
        name: String,
        description: String,
        builder: NullableRoleCommandOptionBuilder.() -> (Unit) = {}
    ) = optionalRole(name, RawStringData(description), builder)

    fun optionalRole(
        name: String,
        description: StringData<*>,
        builder: NullableRoleCommandOptionBuilder.() -> (Unit) = {}
    ) = NullableRoleCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun channel(
        name: String,
        description: String,
        builder: ChannelCommandOptionBuilder.() -> (Unit) = {}
    ) = channel(name, RawStringData(description), builder)

    fun channel(
        name: String,
        description: StringData<*>,
        builder: ChannelCommandOptionBuilder.() -> (Unit) = {}
    ) = ChannelCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun optionalChannel(
        name: String,
        description: String,
        builder: NullableChannelCommandOptionBuilder.() -> (Unit) = {}
    ) = optionalChannel(name, RawStringData(description), builder)

    fun optionalChannel(
        name: String,
        description: StringData<*>,
        builder: NullableChannelCommandOptionBuilder.() -> (Unit) = {}
    ) = NullableChannelCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun mentionable(
        name: String,
        description: String,
        builder: MentionableCommandOptionBuilder.() -> (Unit) = {}
    ) = mentionable(name, RawStringData(description), builder)

    fun mentionable(
        name: String,
        description: StringData<*>,
        builder: MentionableCommandOptionBuilder.() -> (Unit) = {}
    ) = MentionableCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun optionalMentionable(
        name: String,
        description: String,
        builder: NullableMentionableCommandOptionBuilder.() -> (Unit) = {}
    ) = optionalMentionable(name, RawStringData(description), builder)

    fun optionalMentionable(
        name: String,
        description: StringData<*>,
        builder: NullableMentionableCommandOptionBuilder.() -> (Unit) = {}
    ) = NullableMentionableCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun attachment(
        name: String,
        description: String,
        builder: AttachmentCommandOptionBuilder.() -> (Unit) = {}
    ) = attachment(name, RawStringData(description), builder)

    fun attachment(
        name: String,
        description: StringData<*>,
        builder: AttachmentCommandOptionBuilder.() -> (Unit) = {}
    ) = AttachmentCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun optionalAttachment(
        name: String,
        description: String,
        builder: NullableAttachmentCommandOptionBuilder.() -> (Unit) = {}
    ) = optionalAttachment(name, RawStringData(description), builder)

    fun optionalAttachment(
        name: String,
        description: StringData<*>,
        builder: NullableAttachmentCommandOptionBuilder.() -> (Unit) = {}
    ) = NullableAttachmentCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }
}

/**
 * Registers a [optionBuilder] to an [ApplicationCommandOptions]
 *
 * @param optionBuilder the option builder
 * @return an [OptionReference]
 */
inline fun <reified T, ChoiceableType> ApplicationCommandOptions.register(optionBuilder: CommandOptionBuilder<T, ChoiceableType>): OptionReference<T> {
    if (optionBuilders.any { it.name == optionBuilder.name })
        throw IllegalArgumentException("Duplicate argument \"${optionBuilder.name}\"!")

    val optionReference = OptionReference<T>(optionBuilder.name, optionBuilder.required)

    optionBuilders.add(optionBuilder)
    references.add(optionReference)

    return optionReference
}
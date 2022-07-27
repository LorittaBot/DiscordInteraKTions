package net.perfectdreams.discordinteraktions.common.commands.options

open class ApplicationCommandOptions {
    companion object {
        val NO_OPTIONS = object : ApplicationCommandOptions() {}
    }

    val registeredOptions = mutableListOf<InteraKTionsCommandOption<*>>()
    val references = mutableListOf<OptionReference<*>>()

    fun string(
        name: String,
        description: String,
        builder: StringCommandOptionBuilder.() -> (Unit) = {}
    ) = StringCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun optionalString(
        name: String,
        description: String,
        builder: NullableStringCommandOptionBuilder.() -> (Unit) = {}
    ) = NullableStringCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun integer(
        name: String,
        description: String,
        builder: IntegerCommandOptionBuilder.() -> (Unit) = {}
    ) = IntegerCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun optionalInteger(
        name: String,
        description: String,
        builder: NullableIntegerCommandOptionBuilder.() -> (Unit) = {}
    ) = NullableIntegerCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun number(
        name: String,
        description: String,
        builder: NumberCommandOptionBuilder.() -> (Unit) = {}
    ) = NumberCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun optionalNumber(
        name: String,
        description: String,
        builder: NullableNumberCommandOptionBuilder.() -> (Unit) = {}
    ) = NullableNumberCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun boolean(
        name: String,
        description: String,
        builder: BooleanCommandOptionBuilder.() -> (Unit) = {}
    ) = BooleanCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun optionalBoolean(
        name: String,
        description: String,
        builder: NullableBooleanCommandOptionBuilder.() -> (Unit) = {}
    ) = NullableBooleanCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun user(
        name: String,
        description: String,
        builder: UserCommandOptionBuilder.() -> (Unit) = {}
    ) = UserCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun optionalUser(
        name: String,
        description: String,
        builder: NullableUserCommandOptionBuilder.() -> (Unit) = {}
    ) = NullableUserCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun role(
        name: String,
        description: String,
        builder: RoleCommandOptionBuilder.() -> (Unit) = {}
    ) = RoleCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun optionalRole(
        name: String,
        description: String,
        builder: NullableRoleCommandOptionBuilder.() -> (Unit) = {}
    ) = NullableRoleCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun channel(
        name: String,
        description: String,
        builder: ChannelCommandOptionBuilder.() -> (Unit) = {}
    ) = ChannelCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun optionalChannel(
        name: String,
        description: String,
        builder: NullableChannelCommandOptionBuilder.() -> (Unit) = {}
    ) = NullableChannelCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun mentionable(
        name: String,
        description: String,
        builder: MentionableCommandOptionBuilder.() -> (Unit) = {}
    ) = MentionableCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun optionalMentionable(
        name: String,
        description: String,
        builder: NullableMentionableCommandOptionBuilder.() -> (Unit) = {}
    ) = NullableMentionableCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun attachment(
        name: String,
        description: String,
        builder: AttachmentCommandOptionBuilder.() -> (Unit) = {}
    ) = AttachmentCommandOptionBuilder(name, description)
        .apply(builder)
        .let { register(it) }

    fun optionalAttachment(
        name: String,
        description: String,
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
    if (registeredOptions.any { it.name == optionBuilder.name })
        throw IllegalArgumentException("Duplicate argument \"${optionBuilder.name}\"!")

    val optionReference = OptionReference<T>(optionBuilder.name, optionBuilder.required)

    registeredOptions.add(optionBuilder.build())
    references.add(optionReference)

    return optionReference
}
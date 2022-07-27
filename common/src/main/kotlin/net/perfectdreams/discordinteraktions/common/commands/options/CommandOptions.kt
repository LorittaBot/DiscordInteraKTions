package net.perfectdreams.discordinteraktions.common.commands.options

import dev.kord.common.Locale
import dev.kord.common.entity.*
import dev.kord.common.entity.optional.optional
import dev.kord.rest.builder.interaction.*
import net.perfectdreams.discordinteraktions.common.autocomplete.AutocompleteHandler
import net.perfectdreams.discordinteraktions.common.entities.Channel
import net.perfectdreams.discordinteraktions.common.entities.Mentionable
import net.perfectdreams.discordinteraktions.common.entities.Role
import net.perfectdreams.discordinteraktions.common.entities.User
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordChannel
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordRole
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordUser

abstract class InteraKTionsCommandOption<T>(
    val name: String
) {
    abstract fun register(builder: BaseInputChatBuilder)

    abstract fun parse(args: List<CommandArgument<*>>, interaction: DiscordInteraction): T?
}

abstract class NameableCommandOption<T>(
    name: String,
    val description: String,
    val nameLocalizations: Map<Locale, String>?,
    val descriptionLocalizations: Map<Locale, String>?,
) : InteraKTionsCommandOption<T>(name)

abstract class DiscordCommandOption<T>(
    name: String,
    description: String,
    nameLocalizations: Map<Locale, String>?,
    descriptionLocalizations: Map<Locale, String>?,
    val required: Boolean
) : NameableCommandOption<T>(name, description, nameLocalizations, descriptionLocalizations)

abstract class GenericCommandOption<T>(
    name: String,
    description: String,
    nameLocalizations: Map<Locale, String>?,
    descriptionLocalizations: Map<Locale, String>?,
    required: Boolean
) : DiscordCommandOption<T>(name, description, nameLocalizations, descriptionLocalizations, required) {
    override fun parse(args: List<CommandArgument<*>>, interaction: DiscordInteraction): T? {
        return args.firstOrNull { it.name == name }?.value as T
    }
}

interface ChoiceableCommandOption<T> {
    val choices: List<CommandChoice<T>>?
    val autocompleteExecutor: AutocompleteHandler<T>?
}

// ===[ STRING ]===
class StringCommandOption(
    name: String,
    description: String,
    nameLocalizations: Map<Locale, String>?,
    descriptionLocalizations: Map<Locale, String>?,
    required: Boolean,
    override val choices: List<CommandChoice<String>>?,
    val minLength: Int?,
    val maxLength: Int?,
    override val autocompleteExecutor: AutocompleteHandler<String>?
) : GenericCommandOption<String>(name, description, nameLocalizations, descriptionLocalizations, required), ChoiceableCommandOption<String> {
    override fun register(builder: BaseInputChatBuilder) {
        builder.string(this@StringCommandOption.name, this@StringCommandOption.description) {
            this.nameLocalizations = this@StringCommandOption.nameLocalizations?.toMutableMap()
            this.descriptionLocalizations = this@StringCommandOption.descriptionLocalizations?.toMutableMap()
            this.required = this@StringCommandOption.required
            this.autocomplete = this@StringCommandOption.autocompleteExecutor != null
            this.minLength = this@StringCommandOption.minLength
            this.maxLength = this@StringCommandOption.maxLength

            this@StringCommandOption.choices?.forEach { choice ->
                choice(choice.name, choice.value, choice.nameLocalizations.optional())
            }
        }
    }
}

class IntegerCommandOption(
    name: String,
    description: String,
    nameLocalizations: Map<Locale, String>?,
    descriptionLocalizations: Map<Locale, String>?,
    required: Boolean,
    override val choices: List<CommandChoice<Long>>?,
    val minValue: Long?,
    val maxValue: Long?,
    override val autocompleteExecutor: AutocompleteHandler<Long>?
) : GenericCommandOption<Long>(name, description, nameLocalizations, descriptionLocalizations, required), ChoiceableCommandOption<Long> {
    override fun register(builder: BaseInputChatBuilder) {
        builder.int(this@IntegerCommandOption.name, this@IntegerCommandOption.description) {
            this.nameLocalizations = this@IntegerCommandOption.nameLocalizations?.toMutableMap()
            this.descriptionLocalizations = this@IntegerCommandOption.descriptionLocalizations?.toMutableMap()
            this.required = this@IntegerCommandOption.required
            this.autocomplete = this@IntegerCommandOption.autocompleteExecutor != null
            this.minValue = this@IntegerCommandOption.minValue
            this.maxValue = this@IntegerCommandOption.maxValue

            this@IntegerCommandOption.choices?.forEach { choice ->
                choice(choice.name, choice.value, choice.nameLocalizations.optional())
            }
        }
    }
}

class NumberCommandOption(
    name: String,
    description: String,
    nameLocalizations: Map<Locale, String>?,
    descriptionLocalizations: Map<Locale, String>?,
    required: Boolean,
    override val choices: List<CommandChoice<Double>>?,
    val minValue: Double?,
    val maxValue: Double?,
    override val autocompleteExecutor: AutocompleteHandler<Double>?
) : GenericCommandOption<Double>(name, description, nameLocalizations, descriptionLocalizations, required), ChoiceableCommandOption<Double> {
    override fun register(builder: BaseInputChatBuilder) {
        builder.number(this@NumberCommandOption.name, this@NumberCommandOption.description) {
            this.nameLocalizations = this@NumberCommandOption.nameLocalizations?.toMutableMap()
            this.descriptionLocalizations = this@NumberCommandOption.descriptionLocalizations?.toMutableMap()
            this.required = this@NumberCommandOption.required
            this.autocomplete = this@NumberCommandOption.autocompleteExecutor != null
            this.minValue = this@NumberCommandOption.minValue
            this.maxValue = this@NumberCommandOption.maxValue

            this@NumberCommandOption.choices?.forEach { choice ->
                choice(choice.name, choice.value, choice.nameLocalizations.optional())
            }
        }
    }
}

class BooleanCommandOption(
    name: String,
    description: String,
    nameLocalizations: Map<Locale, String>?,
    descriptionLocalizations: Map<Locale, String>?,
    required: Boolean
) : GenericCommandOption<Boolean>(name, description, nameLocalizations, descriptionLocalizations, required) {
    override fun register(builder: BaseInputChatBuilder) {
        builder.boolean(this@BooleanCommandOption.name, this@BooleanCommandOption.description) {
            this.nameLocalizations = this@BooleanCommandOption.nameLocalizations?.toMutableMap()
            this.descriptionLocalizations = this@BooleanCommandOption.descriptionLocalizations?.toMutableMap()
            this.required = this@BooleanCommandOption.required
        }
    }
}

class UserCommandOption(
    name: String,
    description: String,
    nameLocalizations: Map<Locale, String>?,
    descriptionLocalizations: Map<Locale, String>?,
    required: Boolean
) : DiscordCommandOption<User>(name, description, nameLocalizations, descriptionLocalizations, required) {
    override fun register(builder: BaseInputChatBuilder) {
        builder.user(this@UserCommandOption.name, this@UserCommandOption.description) {
            this.nameLocalizations = this@UserCommandOption.nameLocalizations?.toMutableMap()
            this.descriptionLocalizations = this@UserCommandOption.descriptionLocalizations?.toMutableMap()
            this.required = this@UserCommandOption.required
        }
    }

    override fun parse(args: List<CommandArgument<*>>, interaction: DiscordInteraction): User? {
        val userId = args?.firstOrNull { it.name == name }?.value as Snowflake?
        val resolved = interaction.data.resolved.value?.users?.value

        return resolved?.get(userId)?.let { KordUser(it) }
    }
}

class RoleCommandOption(
    name: String,
    description: String,
    nameLocalizations: Map<Locale, String>?,
    descriptionLocalizations: Map<Locale, String>?,
    required: Boolean
) : DiscordCommandOption<Role>(name, description, nameLocalizations, descriptionLocalizations, required) {
    override fun register(builder: BaseInputChatBuilder) {
        builder.role(this@RoleCommandOption.name, this@RoleCommandOption.description) {
            this.nameLocalizations = this@RoleCommandOption.nameLocalizations?.toMutableMap()
            this.descriptionLocalizations = this@RoleCommandOption.descriptionLocalizations?.toMutableMap()
            this.required = this@RoleCommandOption.required
        }
    }

    override fun parse(args: List<CommandArgument<*>>, interaction: DiscordInteraction): Role? {
        val roleId = args?.firstOrNull { it.name == name }?.value as Snowflake?
        val resolved = interaction.data.resolved.value?.roles?.value

        return resolved?.get(roleId)?.let { KordRole(it) }
    }
}

class ChannelCommandOption(
    name: String,
    description: String,
    nameLocalizations: Map<Locale, String>?,
    descriptionLocalizations: Map<Locale, String>?,
    required: Boolean,
    val channelTypes: List<ChannelType>?
) : DiscordCommandOption<Channel>(name, description, nameLocalizations, descriptionLocalizations, required) {
    override fun register(builder: BaseInputChatBuilder) {
        builder.channel(this@ChannelCommandOption.name, this@ChannelCommandOption.description) {
            this.nameLocalizations = this@ChannelCommandOption.nameLocalizations?.toMutableMap()
            this.descriptionLocalizations = this@ChannelCommandOption.descriptionLocalizations?.toMutableMap()
            this.required = this@ChannelCommandOption.required
            this.channelTypes = this@ChannelCommandOption.channelTypes
        }
    }

    override fun parse(args: List<CommandArgument<*>>, interaction: DiscordInteraction): Channel? {
        val channelId = args?.firstOrNull { it.name == name }?.value as Snowflake?
        val resolved = interaction.data.resolved.value?.channels?.value

        return resolved?.get(channelId)?.let { KordChannel(it) }
    }
}

class MentionableCommandOption(
    name: String,
    description: String,
    nameLocalizations: Map<Locale, String>?,
    descriptionLocalizations: Map<Locale, String>?,
    required: Boolean
) : DiscordCommandOption<Mentionable>(name, description, nameLocalizations, descriptionLocalizations, required) {
    override fun register(builder: BaseInputChatBuilder) {
        builder.mentionable(this@MentionableCommandOption.name, this@MentionableCommandOption.description) {
            this.nameLocalizations = this@MentionableCommandOption.nameLocalizations?.toMutableMap()
            this.descriptionLocalizations = this@MentionableCommandOption.descriptionLocalizations?.toMutableMap()
            this.required = this@MentionableCommandOption.required
        }
    }

    override fun parse(
        args: List<CommandArgument<*>>,
        interaction: DiscordInteraction
    ): Mentionable? {
        // Mentionable objects can be User OR Role
        val userId = args?.firstOrNull { it.name == name }?.value as Snowflake?
        val resolvedUser = interaction.data.resolved.value?.users?.value

        val kordUser = resolvedUser?.get(userId)?.let { KordUser(it) }
        if (kordUser != null)
            return kordUser

        val roleId = args?.firstOrNull { it.name == name }?.value as Snowflake?
        val resolvedRole = interaction.data.resolved.value?.roles?.value

        return resolvedRole?.get(roleId)?.let { KordRole(it) }
    }
}

class AttachmentCommandOption(
    name: String,
    description: String,
    nameLocalizations: Map<Locale, String>?,
    descriptionLocalizations: Map<Locale, String>?,
    required: Boolean
) : DiscordCommandOption<DiscordAttachment>(name, description, nameLocalizations, descriptionLocalizations, required) {
    override fun register(builder: BaseInputChatBuilder) {
        builder.attachment(this@AttachmentCommandOption.name, this@AttachmentCommandOption.description) {
            this.nameLocalizations = this@AttachmentCommandOption.nameLocalizations?.toMutableMap()
            this.descriptionLocalizations = this@AttachmentCommandOption.descriptionLocalizations?.toMutableMap()
            this.required = this@AttachmentCommandOption.required
        }
    }

    override fun parse(
        args: List<CommandArgument<*>>,
        interaction: DiscordInteraction
    ): DiscordAttachment? {
        val attachmentId = args?.firstOrNull { it.name == name }?.value as Snowflake?
        return interaction.data.resolved.value?.attachments?.value?.get(attachmentId)
    }
}
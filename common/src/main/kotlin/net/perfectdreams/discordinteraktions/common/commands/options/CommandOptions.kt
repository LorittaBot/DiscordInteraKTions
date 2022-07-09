package net.perfectdreams.discordinteraktions.common.commands.options

import dev.kord.common.Locale
import dev.kord.common.entity.*
import net.perfectdreams.discordinteraktions.common.autocomplete.AutocompleteExecutorDeclaration
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordChannel
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordRole
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordUser

abstract class CommandOption<T>(
    override var name: String,
    override var description: String,
    val required: Boolean
) : OptionBuilder {
    override var nameLocalizations: Map<Locale, String>? = null
    override var descriptionLocalizations: Map<Locale, String>? = null
    override var default: Boolean? = null

    abstract fun parse(args: List<CommandArgument<*>>?, interaction: DiscordInteraction): T
}

abstract class ChoiceableOption<T, S : Any>(name: String, description: String, required: Boolean) :
    CommandOption<T>(name, description, required), ChoiceableOptionBuilder<S> {
    override var choices: MutableList<CommandChoice<S>>? = null
    override var autocomplete: AutocompleteExecutorDeclaration<S>? = null
}

abstract class NumericOption<T, S : Any>(name: String, description: String, required: Boolean) :
    ChoiceableOption<T, S>(name, description, required), NumericOptionBuilder<S> {
    override var minValue: S? = null
    override var maxValue: S? = null
}

class StringOption<T : String?>(name: String, description: String, required: Boolean) :
    ChoiceableOption<T, String>(name, description, required), StringOptionBuilder {
    override var minLength: Int? = null
    override var maxLength: Int? = null

    override fun parse(args: List<CommandArgument<*>>?, interaction: DiscordInteraction): T {
        return args?.firstOrNull { it.name == name }?.value as T
    }
}

class LongOption<T : Long?>(name: String, description: String, required: Boolean) :
    NumericOption<T, Long>(name, description, required), LongOptionBuilder {
    override fun parse(args: List<CommandArgument<*>>?, interaction: DiscordInteraction): T {
        return args?.firstOrNull { it.name == name }?.value as T
    }
}

class DoubleOption<T : Double?>(name: String, description: String, required: Boolean) :
    NumericOption<T, Double>(name, description, required), DoubleOptionBuilder {
    override fun parse(args: List<CommandArgument<*>>?, interaction: DiscordInteraction): T {
        return args?.firstOrNull { it.name == name }?.value as T
    }
}

class BooleanOption<T : Boolean?>(name: String, description: String, required: Boolean) :
    CommandOption<T>(name, description, required), BooleanOptionBuilder {
    override fun parse(args: List<CommandArgument<*>>?, interaction: DiscordInteraction): T {
        return args?.firstOrNull { it.name == name }?.value as T
    }
}

class UserOption<T : KordUser?>(name: String, description: String, required: Boolean) :
    CommandOption<T>(name, description, required), UserOptionBuilder {
    override fun parse(args: List<CommandArgument<*>>?, interaction: DiscordInteraction): T {
        val userId = args?.firstOrNull { it.name == name }?.value as Snowflake?
        val resolved = interaction.data.resolved.value?.users?.value

        return resolved?.get(userId)?.let { KordUser(it) } as T
    }
}

class RoleOption<T : KordRole?>(name: String, description: String, required: Boolean) :
    CommandOption<T>(name, description, required), RoleOptionBuilder {
    override fun parse(args: List<CommandArgument<*>>?, interaction: DiscordInteraction): T {
        val roleId = args?.firstOrNull { it.name == name }?.value as Snowflake?
        val resolved = interaction.data.resolved.value?.roles?.value

        return resolved?.get(roleId)?.let { KordRole(it) } as T
    }
}

class ChannelOption<T : KordChannel?>(name: String, description: String, required: Boolean) :
    CommandOption<T>(name, description, required), ChannelOptionBuilder {
    override var channelTypes: List<ChannelType>? = null

    override fun parse(args: List<CommandArgument<*>>?, interaction: DiscordInteraction): T {
        val channelId = args?.firstOrNull { it.name == name }?.value as Snowflake?
        val resolved = interaction.data.resolved.value?.channels?.value

        return resolved?.get(channelId)?.let { KordChannel(it) } as T
    }
}

class MentionableOption<T : CommandArgument.MentionableArgument?>(
    name: String,
    description: String,
    required: Boolean
) : CommandOption<T>(name, description, required), MentionableOptionBuilder {
    override fun parse(args: List<CommandArgument<*>>?, interaction: DiscordInteraction): T {
        return args?.firstOrNull { it.name == name }?.value as T
    }
}

class AttachmentOption<T : DiscordAttachment?>(
    name: String,
    description: String,
    required: Boolean
) : CommandOption<T>(name, description, required), AttachmentOptionBuilder {
    override fun parse(args: List<CommandArgument<*>>?, interaction: DiscordInteraction): T {
        val attachmentId = args?.firstOrNull { it.name == name }?.value as Snowflake?
        val attachment = interaction.data.resolved.value?.attachments?.value?.get(attachmentId)

        return attachment as T
    }
}

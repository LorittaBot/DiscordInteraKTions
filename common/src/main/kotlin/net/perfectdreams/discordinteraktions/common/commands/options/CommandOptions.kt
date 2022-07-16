package net.perfectdreams.discordinteraktions.common.commands.options

import dev.kord.common.Locale
import dev.kord.common.entity.*
import net.perfectdreams.discordinteraktions.common.autocomplete.AutocompleteExecutorDeclaration
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordChannel
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordRole
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordUser

abstract class CommandOption<T>(
    val name: String,
    val description: String
) : CommandOptionBuilder {
    override var nameLocalizations: Map<Locale, String>? = null
    override var descriptionLocalizations: Map<Locale, String>? = null
    override var default: Boolean? = null
    var required: Boolean = true

    abstract fun parse(args: List<CommandArgument<*>>?, interaction: DiscordInteraction): T
}

abstract class ChoiceableCommandOption<T, S : Any>(name: String, description: String) :
    CommandOption<T>(name, description), ChoiceableCommandOptionBuilder<S> {
    override var choices: MutableList<CommandChoice<S>>? = null
    override var autocomplete: AutocompleteExecutorDeclaration<S>? = null
}

abstract class NumericCommandOption<T, S : Any>(name: String, description: String) :
    ChoiceableCommandOption<T, S>(name, description), NumericCommandOptionBuilder<S> {
    override var minValue: S? = null
    override var maxValue: S? = null
}

class StringCommandOption<T : String?>(name: String, description: String) :
    ChoiceableCommandOption<T, String>(name, description), StringCommandOptionBuilder {
    override var minLength: Int? = null
    override var maxLength: Int? = null

    override fun parse(args: List<CommandArgument<*>>?, interaction: DiscordInteraction): T {
        return args?.firstOrNull { it.name == name }?.value as T
    }
}

class IntegerCommandOption<T : Long?>(name: String, description: String) :
    NumericCommandOption<T, Long>(name, description), IntegerCommandOptionBuilder {
    override fun parse(args: List<CommandArgument<*>>?, interaction: DiscordInteraction): T {
        return args?.firstOrNull { it.name == name }?.value as T
    }
}

class NumberCommandOption<T : Double?>(name: String, description: String) :
    NumericCommandOption<T, Double>(name, description), NumberCommandOptionBuilder {
    override fun parse(args: List<CommandArgument<*>>?, interaction: DiscordInteraction): T {
        return args?.firstOrNull { it.name == name }?.value as T
    }
}

class BooleanCommandOption<T : Boolean?>(name: String, description: String) :
    CommandOption<T>(name, description), BooleanCommandOptionBuilder {
    override fun parse(args: List<CommandArgument<*>>?, interaction: DiscordInteraction): T {
        return args?.firstOrNull { it.name == name }?.value as T
    }
}

class UserCommandOption<T : KordUser?>(name: String, description: String) :
    CommandOption<T>(name, description), UserCommandOptionBuilder {
    override fun parse(args: List<CommandArgument<*>>?, interaction: DiscordInteraction): T {
        val userId = args?.firstOrNull { it.name == name }?.value as Snowflake?
        val resolved = interaction.data.resolved.value?.users?.value

        return resolved?.get(userId)?.let { KordUser(it) } as T
    }
}

class RoleCommandOption<T : KordRole?>(name: String, description: String) :
    CommandOption<T>(name, description), RoleCommandOptionBuilder {
    override fun parse(args: List<CommandArgument<*>>?, interaction: DiscordInteraction): T {
        val roleId = args?.firstOrNull { it.name == name }?.value as Snowflake?
        val resolved = interaction.data.resolved.value?.roles?.value

        return resolved?.get(roleId)?.let { KordRole(it) } as T
    }
}

class ChannelCommandOption<T : KordChannel?>(name: String, description: String) :
    CommandOption<T>(name, description), ChannelCommandOptionBuilder {
    override var channelTypes: List<ChannelType>? = null

    override fun parse(args: List<CommandArgument<*>>?, interaction: DiscordInteraction): T {
        val channelId = args?.firstOrNull { it.name == name }?.value as Snowflake?
        val resolved = interaction.data.resolved.value?.channels?.value

        return resolved?.get(channelId)?.let { KordChannel(it) } as T
    }
}

class MentionableCommandOption<T : CommandArgument.MentionableArgument?>(
    name: String,
    description: String
) : CommandOption<T>(name, description), MentionableCommandOptionBuilder {
    override fun parse(args: List<CommandArgument<*>>?, interaction: DiscordInteraction): T {
        return args?.firstOrNull { it.name == name }?.value as T
    }
}

class AttachmentCommandOption<T : DiscordAttachment?>(
    name: String,
    description: String
) : CommandOption<T>(name, description), AttachmentCommandOptionBuilder {
    override fun parse(args: List<CommandArgument<*>>?, interaction: DiscordInteraction): T {
        val attachmentId = args?.firstOrNull { it.name == name }?.value as Snowflake?
        val attachment = interaction.data.resolved.value?.attachments?.value?.get(attachmentId)

        return attachment as T
    }
}

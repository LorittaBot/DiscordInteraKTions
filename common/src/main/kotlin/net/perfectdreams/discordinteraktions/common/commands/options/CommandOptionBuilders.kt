package net.perfectdreams.discordinteraktions.common.commands.options

import dev.kord.common.Locale
import dev.kord.common.entity.ChannelType
import net.perfectdreams.discordinteraktions.common.autocomplete.AutocompleteExecutorDeclaration

interface CommandOptionBuilder {
    var nameLocalizations: Map<Locale, String>?
    var descriptionLocalizations: Map<Locale, String>?
    var default: Boolean?
}
interface ChoiceableCommandOptionBuilder<T> : CommandOptionBuilder {
    var choices: MutableList<CommandChoice<T>>?
    var autocomplete: AutocompleteExecutorDeclaration<T>?

    fun choice(name: String, value: T, block: CommandChoiceBuilder.() -> (Unit) = {}) {
        require(autocomplete == null) {
            "You can't use pre-defined choices with an autocomplete executor set!"
        }
        val builder = CommandChoiceBuilder().apply(block)

        if (choices == null)
            choices = mutableListOf()
        choices?.add(CommandChoice(name, value, builder.nameLocalizations))
    }

    fun autocomplete(declaration: AutocompleteExecutorDeclaration<T>) {
        autocomplete = declaration
    }
}

interface NumericCommandOptionBuilder<T : Any> : ChoiceableCommandOptionBuilder<T> {
    var minValue: T?
    var maxValue: T?
}

interface IntegerCommandOptionBuilder : NumericCommandOptionBuilder<Long>

interface NumberCommandOptionBuilder : NumericCommandOptionBuilder<Double>

class CommandChoiceBuilder {
    var nameLocalizations: Map<Locale, String>? = null
}

interface StringCommandOptionBuilder : ChoiceableCommandOptionBuilder<String> {
    var minLength: Int?
    var maxLength: Int?
}

interface BooleanCommandOptionBuilder : CommandOptionBuilder

interface UserCommandOptionBuilder : CommandOptionBuilder

interface RoleCommandOptionBuilder : CommandOptionBuilder

interface ChannelCommandOptionBuilder : CommandOptionBuilder {
    var channelTypes: List<ChannelType>?
}

interface MentionableCommandOptionBuilder : CommandOptionBuilder

interface AttachmentCommandOptionBuilder : CommandOptionBuilder

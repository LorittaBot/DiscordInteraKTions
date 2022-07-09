package net.perfectdreams.discordinteraktions.common.commands.options

import dev.kord.common.Locale
import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.DiscordAttachment
import net.perfectdreams.discordinteraktions.common.autocomplete.AutocompleteExecutor
import net.perfectdreams.discordinteraktions.common.entities.Channel
import net.perfectdreams.discordinteraktions.common.entities.Role
import net.perfectdreams.discordinteraktions.common.entities.User
import net.perfectdreams.discordinteraktions.common.autocomplete.AutocompleteExecutorDeclaration

interface OptionBuilder {
    var name: String
    var nameLocalizations: Map<Locale, String>?
    var description: String
    var descriptionLocalizations: Map<Locale, String>?
    var default: Boolean?
}
interface ChoiceableOptionBuilder<T> : OptionBuilder {
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

interface NumericOptionBuilder<T : Any> : ChoiceableOptionBuilder<T> {
    var minValue: T?
    var maxValue: T?
}

interface LongOptionBuilder : NumericOptionBuilder<Long>

interface DoubleOptionBuilder : NumericOptionBuilder<Double>

class CommandChoiceBuilder {
    var nameLocalizations: Map<Locale, String>? = null
}

interface StringOptionBuilder : ChoiceableOptionBuilder<String> {
    var minLength: Int?
    var maxLength: Int?
}

interface BooleanOptionBuilder : OptionBuilder

interface UserOptionBuilder : OptionBuilder

interface RoleOptionBuilder : OptionBuilder

interface ChannelOptionBuilder : OptionBuilder {
    var channelTypes: List<ChannelType>?
}

interface MentionableOptionBuilder : OptionBuilder

interface AttachmentOptionBuilder : OptionBuilder

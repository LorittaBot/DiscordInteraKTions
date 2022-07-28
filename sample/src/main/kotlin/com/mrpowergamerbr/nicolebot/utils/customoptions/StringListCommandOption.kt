package com.mrpowergamerbr.nicolebot.utils.customoptions

import dev.kord.common.Locale
import dev.kord.common.entity.CommandArgument
import dev.kord.common.entity.DiscordInteraction
import dev.kord.rest.builder.interaction.BaseInputChatBuilder
import dev.kord.rest.builder.interaction.string
import net.perfectdreams.discordinteraktions.common.commands.options.*

// A custom "String List" command option implementation, showing off how to implement your own custom options
// While for your code it looks like a List<String>, "behind the scenes" it is actually multiple string options

// ===[ OPTION ]===
class StringListCommandOption(
    override val name: String,
    override val description: String,
    override val nameLocalizations: Map<Locale, String>?,
    override val descriptionLocalizations: Map<Locale, String>?,
    val minimum: Int, // How many options are required
    val maximum: Int // Maximum options generated
) : NameableCommandOption<List<String>>() {
    override fun register(builder: BaseInputChatBuilder) {
        for (it in 1..maximum) {
            builder.string("${name}$it", description) {
                this.nameLocalizations = this@StringListCommandOption.nameLocalizations?.toMutableMap()
                this.descriptionLocalizations = this@StringListCommandOption.descriptionLocalizations?.toMutableMap()
                this.required = minimum >= it
            }
        }
    }

    override fun parse(
        args: List<CommandArgument<*>>,
        interaction: DiscordInteraction
    ): List<String> {
        val listsValues = args.filter { opt -> opt.name.startsWith(name) }

        return listsValues.map { it.value as String }
    }
}

// ===[ BUILDER ]===
class StringListCommandOptionBuilder(
    override val name: String,
    override val description: String,
    private val minimum: Int,
    private val maximum: Int
) : DiscordCommandOptionBuilder<List<String>, List<String>>() {
    override val required = true

    override fun build() = StringListCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations,
        minimum,
        maximum
    )
}

// ===[ EXTENSION METHODS ]===
fun ApplicationCommandOptions.stringList(
    name: String,
    description: String,
    minimum: Int,
    maximum: Int,
    builder: StringListCommandOptionBuilder.() -> (Unit) = {}
) = StringListCommandOptionBuilder(name, description, minimum, maximum)
    .apply(builder)
    .let { register(it) }
package com.mrpowergamerbr.nicolebot.utils.customoptions

import dev.kord.common.Locale
import dev.kord.common.entity.CommandArgument
import dev.kord.common.entity.DiscordInteraction
import dev.kord.rest.builder.interaction.BaseInputChatBuilder
import dev.kord.rest.builder.interaction.string
import kotlinx.coroutines.delay
import net.perfectdreams.discordinteraktions.common.commands.options.ApplicationCommandOptions
import net.perfectdreams.discordinteraktions.common.commands.options.CommandOptionBuilder
import net.perfectdreams.discordinteraktions.common.commands.options.NameableCommandOption
import net.perfectdreams.discordinteraktions.common.commands.options.register

// A custom "Delayed Suspendable" command option, showing off how you can parse options that may require a "suspend" call
// This is useful if you need to pull information from the CommandContext, or if you need to call a suspend method
// To do this, we create a "fake" object reference

// ===[ DATA ]===
data class SuspendableData(val text: String) {
    suspend fun parse(): String {
        delay(5_000) // Something intensive here
        return text.reversed()
    }
}

// ===[ OPTION ]===
class ImageReferenceCommandOption(
    name: String,
    description: String,
    nameLocalizations: Map<Locale, String>?,
    descriptionLocalizations: Map<Locale, String>?
) : NameableCommandOption<SuspendableData>(name, description, nameLocalizations, descriptionLocalizations) {
    override fun register(builder: BaseInputChatBuilder) {
        builder.string(name, description) {
            this.nameLocalizations = this@ImageReferenceCommandOption.nameLocalizations?.toMutableMap()
            this.descriptionLocalizations = this@ImageReferenceCommandOption.descriptionLocalizations?.toMutableMap()
            this.required = true
        }
    }

    override fun parse(
        args: List<CommandArgument<*>>,
        interaction: DiscordInteraction
    ): SuspendableData {
        val input = args.firstOrNull { it.name == name }?.value as String
        return SuspendableData(input)
    }
}

// ===[ BUILDER ]===
class DelayedSuspendableCommandOptionBuilder(
    name: String,
    description: String
) : CommandOptionBuilder<SuspendableData, SuspendableData>(name, description, true) {
    override fun build() = ImageReferenceCommandOption(
        name,
        description,
        nameLocalizations,
        descriptionLocalizations
    )
}

// ===[ EXTENSION METHODS ]===
fun ApplicationCommandOptions.delayedSuspendable(
    name: String,
    description: String,
    builder: DelayedSuspendableCommandOptionBuilder.() -> (Unit) = {}
) = DelayedSuspendableCommandOptionBuilder(name, description)
    .apply(builder)
    .let { register(it) }
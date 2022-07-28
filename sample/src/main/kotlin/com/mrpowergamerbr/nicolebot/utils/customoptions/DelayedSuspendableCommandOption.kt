package com.mrpowergamerbr.nicolebot.utils.customoptions

import dev.kord.common.Locale
import dev.kord.common.entity.CommandArgument
import dev.kord.common.entity.DiscordInteraction
import dev.kord.rest.builder.interaction.BaseInputChatBuilder
import dev.kord.rest.builder.interaction.string
import kotlinx.coroutines.delay
import net.perfectdreams.discordinteraktions.common.commands.options.*

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
class DelayedSuspendableCommandOption(
    override val name: String,
    override val description: String,
    override val nameLocalizations: Map<Locale, String>?,
    override val descriptionLocalizations: Map<Locale, String>?
) : NameableCommandOption<SuspendableData>() {
    override fun register(builder: BaseInputChatBuilder) {
        builder.string(name, description) {
            this.nameLocalizations = this@DelayedSuspendableCommandOption.nameLocalizations?.toMutableMap()
            this.descriptionLocalizations = this@DelayedSuspendableCommandOption.descriptionLocalizations?.toMutableMap()
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
    override val name: String,
    override val description: String
) : DiscordCommandOptionBuilder<SuspendableData, SuspendableData>() {
    override val required = true

    override fun build() = DelayedSuspendableCommandOption(
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
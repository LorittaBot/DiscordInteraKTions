package net.perfectdreams.discordinteraktions.common.modals.components

import dev.kord.common.entity.CommandArgument
import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.TextInputStyle
import dev.kord.rest.builder.interaction.BaseInputChatBuilder

abstract class InteraKTionsModalComponent<T>(
    val customId: String,
    val required: Boolean
) {
    abstract fun register(builder: BaseInputChatBuilder)

    abstract fun parse(args: List<CommandArgument<*>>, interaction: DiscordInteraction): T?
}

class TextInputModalComponent(
    customId: String,
    required: Boolean,
    val style: TextInputStyle,
    val minLength: Int?,
    val maxLength: Int?
) : InteraKTionsModalComponent<String>(customId, required) {
    override fun register(builder: BaseInputChatBuilder) {
        TODO("Not yet implemented")
    }

    override fun parse(args: List<CommandArgument<*>>, interaction: DiscordInteraction): String? {
        TODO("Not yet implemented")
    }
}
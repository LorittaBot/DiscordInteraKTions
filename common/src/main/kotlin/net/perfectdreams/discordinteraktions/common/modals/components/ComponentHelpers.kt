package net.perfectdreams.discordinteraktions.common.modals.components

import dev.kord.common.entity.TextInputStyle
import dev.kord.rest.builder.component.ActionRowBuilder
import dev.kord.rest.builder.component.TextInputBuilder

fun ActionRowBuilder.textInput(
    option: TextInputModalComponent<*>,
    style: TextInputStyle,
    label: String,
    block: TextInputComponentBuilder.() -> (Unit) = {}
) {
    val builder = TextInputComponentBuilder().apply(block)

   this.textInput(style, option.customId, label) {
       required = option.required
       placeholder = builder.placeholder
       allowedLength = builder.allowedLength
   }
}
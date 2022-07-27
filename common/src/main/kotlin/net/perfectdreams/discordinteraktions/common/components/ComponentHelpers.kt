package net.perfectdreams.discordinteraktions.common.components

import dev.kord.common.entity.ButtonStyle
import dev.kord.rest.builder.component.ActionRowBuilder
import dev.kord.rest.builder.component.ButtonBuilder
import dev.kord.rest.builder.component.SelectMenuBuilder

fun ActionRowBuilder.interactiveButton(
    style: ButtonStyle,
    executor: ButtonExecutorDeclaration,
    builder: ButtonBuilder.InteractionButtonBuilder.() -> Unit
) {
    require(style != ButtonStyle.Link) { "You cannot use a ButtonStyle.Link style in a interactive button! Please use \"linkButton(...)\" if you want to create a button with a link" }

    interactionButton(
        style,
        executor.id
    ) {
        builder.invoke(this)
    }
}

fun ActionRowBuilder.interactiveButton(
    style: ButtonStyle,
    label: String,
    executor: ButtonExecutorDeclaration,
    builder: ButtonBuilder.InteractionButtonBuilder.() -> Unit = {}
) {
    require(style != ButtonStyle.Link) { "You cannot use a ButtonStyle.Link style in a interactive button! Please use \"linkButton(...)\" if you want to create a button with a link" }

    interactionButton(
        style,
        executor.id
    ) {
        this.label = label
        builder.invoke(this)
    }
}

fun ActionRowBuilder.interactiveButton(
    style: ButtonStyle,
    executor: ButtonExecutorDeclaration,
    data: String,
    builder: ButtonBuilder.InteractionButtonBuilder.() -> Unit
) {
    require(style != ButtonStyle.Link) { "You cannot use a ButtonStyle.Link style in a interactive button! Please use \"linkButton(...)\" if you want to create a button with a link" }

    interactionButton(
        style,
        "${executor.id}:$data"
    ) {
        builder.invoke(this)
    }
}

fun ActionRowBuilder.interactiveButton(
    style: ButtonStyle,
    label: String,
    executor: ButtonExecutorDeclaration,
    data: String,
    builder: ButtonBuilder.InteractionButtonBuilder.() -> Unit = {}
) {
    require(style != ButtonStyle.Link) { "You cannot use a ButtonStyle.Link style in a interactive button! Please use \"linkButton(...)\" if you want to create a button with a link" }

    interactionButton(
        style,
        "${executor.id}:$data"
    ) {
        this.label = label
        builder.invoke(this)
    }
}

fun ActionRowBuilder.selectMenu(
    executor: SelectMenuExecutorDeclaration,
    builder: SelectMenuBuilder.() -> Unit = {}
) {
    selectMenu(
        executor.id
    ) {
        builder.invoke(this)
    }
}

fun ActionRowBuilder.selectMenu(
    executor: SelectMenuExecutorDeclaration,
    data: String,
    builder: SelectMenuBuilder.() -> Unit = {}
) {
    selectMenu(
        "${executor.id}:$data"
    ) {
        builder.invoke(this)
    }
}
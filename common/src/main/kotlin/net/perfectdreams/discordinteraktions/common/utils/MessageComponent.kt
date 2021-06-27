package net.perfectdreams.discordinteraktions.common.utils

import kotlin.reflect.KClass

sealed class MessageComponent(val type: Int)

class ActionRowComponent(
    val components: List<MessageComponent>
) : MessageComponent(1)

open class ButtonComponent(
    val style: ButtonStyle,
    val label: String,
    val customId: String? = null,
    val url: String? = null,
    val disabled: Boolean = false,
) : MessageComponent(2)
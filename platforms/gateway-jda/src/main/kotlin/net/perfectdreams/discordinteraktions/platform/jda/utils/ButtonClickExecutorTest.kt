package net.perfectdreams.discordinteraktions.platform.jda.utils

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.context.buttons.ButtonClickContext
import net.perfectdreams.discordinteraktions.common.context.commands.SlashCommandContext
import net.perfectdreams.discordinteraktions.common.utils.ButtonClickExecutor
import net.perfectdreams.discordinteraktions.common.utils.TestData

class ButtonClickExecutorTest : ButtonClickExecutor<TestData> {
    override suspend fun onClickConvertToProperData(user: User, context: ButtonClickContext, data: Any?) {
        onClick(user, context, Json.decodeFromString(data as String))
    }

    override suspend fun onClick(user: User, context: ButtonClickContext, data: TestData) {
        /* context.sendMessage {
            content = "eeeeeesse é meu patrão hehe, string guardada: ${data.str}"
        } */
    }
}
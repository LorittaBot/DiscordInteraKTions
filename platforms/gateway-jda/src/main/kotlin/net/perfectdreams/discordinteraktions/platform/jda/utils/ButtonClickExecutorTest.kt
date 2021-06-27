package net.perfectdreams.discordinteraktions.platform.jda.utils

import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.context.commands.SlashCommandContext
import net.perfectdreams.discordinteraktions.common.utils.ButtonClickExecutor
import net.perfectdreams.discordinteraktions.common.utils.TestData

class ButtonClickExecutorTest : ButtonClickExecutor<TestData> {
    override suspend fun onClick(user: User, context: SlashCommandContext, data: TestData) {
        context.sendMessage {
            content = "eeeeeesse é meu patrão hehe, string guardada: ${data.str}"
        }
    }
}
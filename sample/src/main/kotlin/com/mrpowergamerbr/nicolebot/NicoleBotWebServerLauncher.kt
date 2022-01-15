package com.mrpowergamerbr.nicolebot

import kotlinx.coroutines.runBlocking
import net.perfectdreams.discordinteraktions.webserver.InteractionsServer
import java.io.File

object NicoleBotWebServerLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking {
            val interactionsServer = InteractionsServer(
                NicoleBot.APPLICATION_ID,
                File("public-key.txt").readText(),
                File("token.txt").readText(),
                12212
            )

            val nicoleBot = NicoleBot(
                interactionsServer.rest,
                interactionsServer.commandManager
            )

            nicoleBot.registerCommands()
        }
    }
}
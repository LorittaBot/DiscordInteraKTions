package com.mrpowergamerbr.nicolebot

import kotlinx.coroutines.runBlocking
import net.perfectdreams.discordinteraktions.webserver.InteractionsServer
import java.io.File

object NicoleBotWebServerLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking {
            val token = File("token.txt").readText()
            val publicKey = File("public-key.txt").readText()

            val nicoleBot = NicoleBot(token)
            nicoleBot.registerCommands()

            val interactionsServer = InteractionsServer(
                nicoleBot.interaKTions,
                publicKey,
                12212
            )

            interactionsServer.start()
        }
    }
}
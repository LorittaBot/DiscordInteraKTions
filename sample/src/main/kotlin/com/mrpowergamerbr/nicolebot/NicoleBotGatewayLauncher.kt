package com.mrpowergamerbr.nicolebot

import dev.kord.gateway.DefaultGateway
import dev.kord.gateway.start
import dev.kord.rest.service.RestClient
import kotlinx.coroutines.runBlocking
import net.perfectdreams.discordinteraktions.common.commands.CommandManager
import net.perfectdreams.discordinteraktions.platforms.kord.installDiscordInteraKTions
import java.io.File

object NicoleBotGatewayLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking {
            val rest = RestClient(File("token.txt").readText())
            val commandManager = CommandManager()

            val nicoleBot = NicoleBot(
                rest,
                commandManager
            )

            nicoleBot.registerCommands()

            val gateway = DefaultGateway {}

            gateway.installDiscordInteraKTions(
                NicoleBot.APPLICATION_ID,
                rest,
                commandManager
            )

            gateway.start(File("token.txt").readText())
        }
    }
}
package net.perfectdreams.discordinteraktions.utils

import net.perfectdreams.discordinteraktions.InteractionsServer
import net.perfectdreams.discordinteraktions.commands.CommandManager
import net.perfectdreams.discordinteraktions.commands.SlashCommand

/**
 * This is a shorter way of registering commands
 * on the server's [CommandManager].
 *
 * @param command The command that'll be registered.
 */
fun InteractionsServer.register(command: SlashCommand) =
    commandManager.commands.add(command)
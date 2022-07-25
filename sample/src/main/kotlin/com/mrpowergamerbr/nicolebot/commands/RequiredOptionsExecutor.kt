package com.mrpowergamerbr.nicolebot.commands

import dev.kord.common.entity.ChannelType
import net.perfectdreams.discordinteraktions.common.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.commands.options.ApplicationCommandOptions
import net.perfectdreams.discordinteraktions.common.commands.options.SlashCommandArguments

class RequiredOptionsExecutor : SlashCommandExecutor() {
    companion object : SlashCommandExecutorDeclaration() {
        object Options : ApplicationCommandOptions() {
            val example = string("string", "String")

            val integer = integer("integer", "Integer") {
                range = 5..100L
            }

            val number = number("number", "Number") {
                range = 5.0..10.0
            }

            val boolean = boolean("boolean", "Boolean")

            val user = user("user", "User")

            val role = role("role", "Role")

            val channel = channel("channel", "Channel") {
                channelTypes = listOf(
                    ChannelType.GuildText
                )
            }

            val mentionable = mentionable("mentionable", "Mentionable")

            val attachment = attachment("attachment", "Attachment")
        }

        override val options = Options
    }

    override suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments) {
        context.sendMessage {
            content = buildString {
                append("**Options:**")
                append("\n")
                append("${Options.example.name}: ${args[Options.example]}")
                append("\n")
                append("${Options.integer.name}: ${args[Options.integer]}")
                append("\n")
                append("${Options.number.name}: ${args[Options.number]}")
                append("\n")
                append("${Options.boolean.name}: ${args[Options.boolean]}")
                append("\n")
                append("${Options.user.name}: ${args[Options.user]}")
                append("\n")
                append("${Options.role.name}: ${args[Options.role]}")
                append("\n")
                append("${Options.channel.name}: ${args[Options.channel]}")
                append("\n")
                append("${Options.mentionable.name}: ${args[Options.mentionable]}")
                append("\n")
                append("${Options.attachment.name}: ${args[Options.attachment]}")
            }
        }
    }
}
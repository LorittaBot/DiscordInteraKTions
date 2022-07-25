package com.mrpowergamerbr.nicolebot.commands

import dev.kord.common.entity.ChannelType
import net.perfectdreams.discordinteraktions.common.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutorDeclaration
import net.perfectdreams.discordinteraktions.common.commands.options.ApplicationCommandOptions
import net.perfectdreams.discordinteraktions.common.commands.options.SlashCommandArguments

class OptionalOptionsExecutor : SlashCommandExecutor() {
    companion object : SlashCommandExecutorDeclaration() {
        object Options : ApplicationCommandOptions() {
            val example = optionalString("string", "String")

            val integer = optionalInteger("integer", "Integer") {
                range = 5..100L
            }

            val number = optionalNumber("number", "Number") {
                range = 5.0..10.0
            }

            val boolean = optionalBoolean("boolean", "Boolean")

            val user = optionalUser("user", "User")

            val role = optionalRole("role", "Role")

            val channel = optionalChannel("channel", "Channel") {
                channelTypes = listOf(
                    ChannelType.GuildText
                )
            }

            val mentionable = optionalMentionable("mentionable", "Mentionable")

            val attachment = optionalAttachment("attachment", "Attachment")
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
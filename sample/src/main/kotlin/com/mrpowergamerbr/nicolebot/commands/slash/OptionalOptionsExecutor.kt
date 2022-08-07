package com.mrpowergamerbr.nicolebot.commands.slash

import dev.kord.common.entity.ChannelType
import net.perfectdreams.discordinteraktions.common.commands.ApplicationCommandContext
import net.perfectdreams.discordinteraktions.common.commands.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.common.commands.options.ApplicationCommandOptions
import net.perfectdreams.discordinteraktions.common.commands.options.SlashCommandArguments

class OptionalOptionsExecutor : SlashCommandExecutor() {
    inner class Options : ApplicationCommandOptions() {
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

    override val options = Options()

    override suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments) {
        context.sendMessage {
            content = buildString {
                append("**Options:**")
                append("\n")
                // We are going to limit to 100 characters each option to avoid overflowing the message length
                append("${options.example.name}: ${args[options.example]}".take(100))
                append("\n")
                append("${options.integer.name}: ${args[options.integer]}".take(100))
                append("\n")
                append("${options.number.name}: ${args[options.number]}".take(100))
                append("\n")
                append("${options.boolean.name}: ${args[options.boolean]}".take(100))
                append("\n")
                append("${options.user.name}: ${args[options.user]}".take(100))
                append("\n")
                append("${options.role.name}: ${args[options.role]}".take(100))
                append("\n")
                append("${options.channel.name}: ${args[options.channel]}".take(100))
                append("\n")
                append("${options.mentionable.name}: ${args[options.mentionable]}".take(100))
                append("\n")
                append("${options.attachment.name}: ${args[options.attachment]}".take(100))
            }
        }
    }
}
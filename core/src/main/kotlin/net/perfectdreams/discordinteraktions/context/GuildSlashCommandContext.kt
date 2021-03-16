package net.perfectdreams.discordinteraktions.context

import dev.kord.common.entity.Option
import net.perfectdreams.discordinteraktions.api.entities.Member
import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.entities.CommandInteraction
import net.perfectdreams.discordinteraktions.internal.entities.KordUser
import net.perfectdreams.discordinteraktions.api.entities.Message
import net.perfectdreams.discordinteraktions.commands.SlashCommand
import net.perfectdreams.discordinteraktions.internal.entities.KordMember
import net.perfectdreams.discordinteraktions.utils.InteractionMessage
import net.perfectdreams.discordinteraktions.utils.MessageBuilder
import net.perfectdreams.discordinteraktions.utils.buildMessage

class GuildSlashCommandContext(
    command: SlashCommand,
    request: CommandInteraction,
    rootOptions: List<Option>?,
    bridge: RequestBridge
) : SlashCommandContext(command, request, rootOptions, bridge) {
    val member: Member = KordMember(request.member.value ?: throw IllegalArgumentException("There isn't a member object present! Discord Bug?"))
}
package net.perfectdreams.discordinteraktions.context

import dev.kord.common.entity.Option
import net.perfectdreams.discordinteraktions.api.entities.Member
import net.perfectdreams.discordinteraktions.entities.CommandInteraction
import net.perfectdreams.discordinteraktions.commands.SlashCommandExecutor
import net.perfectdreams.discordinteraktions.internal.entities.KordMember

class GuildSlashCommandContext(
    request: CommandInteraction,
    rootOptions: List<Option>?,
    bridge: RequestBridge
) : SlashCommandContext(request, rootOptions, bridge) {
    val member: Member = KordMember(request.member.value ?: throw IllegalArgumentException("There isn't a member object present! Discord Bug?"))
}
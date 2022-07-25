package net.perfectdreams.discordinteraktions.common.commands

import dev.kord.common.Locale
import dev.kord.common.entity.Permissions
import net.perfectdreams.discordinteraktions.common.stringhandlers.RawStringData
import net.perfectdreams.discordinteraktions.common.stringhandlers.StringData
import net.perfectdreams.discordinteraktions.common.stringhandlers.StringDataHandlers
import net.perfectdreams.discordinteraktions.common.utils.InteraKTionsDslMarker

// ===[ SLASH COMMANDS ]===
fun slashCommand(name: String, description: String, block: SlashCommandDeclarationBuilder.() -> (Unit)) = slashCommand(
    RawStringData(name),
    RawStringData(description),
    block
)

fun slashCommand(name: StringData<*>, description: StringData<*>, block: SlashCommandDeclarationBuilder.() -> (Unit)) = SlashCommandDeclarationBuilder(name, description)
    .apply(block)

@InteraKTionsDslMarker
class SlashCommandDeclarationBuilder(
    val name: StringData<*>,
    val description: StringData<*>
) {
    var nameLocalizations: Map<Locale, String>? = null
    var descriptionLocalizations: Map<Locale, String>? = null
    var executor: SlashCommandExecutorDeclaration? = null
    val subcommands = mutableListOf<SlashCommandDeclarationBuilder>()
    val subcommandGroups = mutableListOf<SlashCommandGroupDeclarationBuilder>()
    // Only root commands can have permissions and dmPermission
    var defaultMemberPermissions: Permissions? = null
    var dmPermission: Boolean? = null

    fun subcommand(name: String, description: String, block: SlashCommandDeclarationBuilder.() -> (Unit)) {
        subcommands += SlashCommandDeclarationBuilder(RawStringData(name), RawStringData(description)).apply(block)
    }

    fun subcommandGroup(name: String, description: String, block: SlashCommandGroupDeclarationBuilder.() -> (Unit)) {
        subcommandGroups += SlashCommandGroupDeclarationBuilder(RawStringData(name), RawStringData(description)).apply(block)
    }

    fun build(handlers: StringDataHandlers): SlashCommandDeclaration {
        val options = executor?.options?.optionBuilders

        return SlashCommandDeclaration(
            handlers.provide(name),
            nameLocalizations,
            handlers.provide(description),
            descriptionLocalizations,
            executor,
            options?.map { it.build(handlers) },
            defaultMemberPermissions,
            dmPermission,
            subcommands.map { it.build(handlers) },
            subcommandGroups.map { it.build(handlers) }
        )
    }
}

@InteraKTionsDslMarker
class SlashCommandGroupDeclarationBuilder(val name: StringData<*>, val description: StringData<*>) {
    var nameLocalizations: Map<Locale, String>? = null
    var descriptionLocalizations: Map<Locale, String>? = null
    // Groups can't have executors!
    val subcommands = mutableListOf<SlashCommandDeclarationBuilder>()

    fun subcommand(name: String, description: String, block: SlashCommandDeclarationBuilder.() -> (Unit)) {
        subcommands += SlashCommandDeclarationBuilder(RawStringData(name), RawStringData(description)).apply(block)
    }

    fun build(handlers: StringDataHandlers): SlashCommandGroupDeclaration {
        return SlashCommandGroupDeclaration(
            handlers.provide(name),
            nameLocalizations,
            handlers.provide(description),
            descriptionLocalizations,
            subcommands.map { it.build(handlers) }
        )
    }
}

// ===[ USER COMMANDS ]===
fun userCommand(name: String, executor: UserCommandExecutorDeclaration) = userCommand(RawStringData(name), executor)

fun userCommand(name: StringData<*>, executor: UserCommandExecutorDeclaration) = UserCommandDeclarationBuilder(name, executor)

@InteraKTionsDslMarker
class UserCommandDeclarationBuilder(val name: StringData<*>, val executor: UserCommandExecutorDeclaration) {
    var nameLocalizations: Map<Locale, String>? = null
    var defaultMemberPermissions: Permissions? = null
    var dmPermission: Boolean? = null

    fun build(handlers: StringDataHandlers): UserCommandDeclaration {
        return UserCommandDeclaration(
            handlers.provide(name),
            nameLocalizations,
            defaultMemberPermissions,
            dmPermission,
            executor
        )
    }
}

// ===[ MESSAGE COMMANDS ]===
fun messageCommand(name: String, executor: MessageCommandExecutorDeclaration) = messageCommand(RawStringData(name), executor)

fun messageCommand(name: StringData<*>, executor: MessageCommandExecutorDeclaration) = MessageCommandDeclarationBuilder(name, executor)

@InteraKTionsDslMarker
class MessageCommandDeclarationBuilder(val name: StringData<*>, val executor: MessageCommandExecutorDeclaration) {
    var nameLocalizations: Map<Locale, String>? = null
    var defaultMemberPermissions: Permissions? = null
    var dmPermission: Boolean? = null

    fun build(handlers: StringDataHandlers): MessageCommandDeclaration {
        return MessageCommandDeclaration(
            handlers.provide(name),
            nameLocalizations,
            defaultMemberPermissions,
            dmPermission,
            executor
        )
    }
}
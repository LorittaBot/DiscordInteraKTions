package net.perfectdreams.discordinteraktions.common.commands

import dev.kord.common.Locale
import dev.kord.common.entity.Permissions
import net.perfectdreams.discordinteraktions.common.utils.InteraKTionsDslMarker

// ===[ SLASH COMMANDS ]===
fun slashCommand(name: String, description: String, block: SlashCommandDeclarationBuilder.() -> (Unit)) = SlashCommandDeclarationBuilder(
    name,
    description
).apply(block)

@InteraKTionsDslMarker
class SlashCommandDeclarationBuilder(
    val name: String,
    val description: String
) {
    var nameLocalizations: Map<Locale, String>? = null
    var descriptionLocalizations: Map<Locale, String>? = null
    var executor: SlashCommandExecutor? = null
    val subcommands = mutableListOf<SlashCommandDeclarationBuilder>()
    val subcommandGroups = mutableListOf<SlashCommandGroupDeclarationBuilder>()
    // Only root commands can have permissions and dmPermission
    var defaultMemberPermissions: Permissions? = null
    var dmPermission: Boolean? = null

    fun subcommand(name: String, description: String, block: SlashCommandDeclarationBuilder.() -> (Unit)) {
        subcommands += SlashCommandDeclarationBuilder(name, description).apply(block)
    }

    fun subcommandGroup(name: String, description: String, block: SlashCommandGroupDeclarationBuilder.() -> (Unit)) {
        subcommandGroups += SlashCommandGroupDeclarationBuilder(name, description).apply(block)
    }

    fun build(): SlashCommandDeclaration {
        return InteraKTionsSlashCommandDeclaration(
            name,
            nameLocalizations,
            description,
            descriptionLocalizations,
            executor,
            defaultMemberPermissions,
            dmPermission,
            subcommands.map { it.build() },
            subcommandGroups.map { it.build() }
        )
    }
}

@InteraKTionsDslMarker
class SlashCommandGroupDeclarationBuilder(
    val name: String,
    val description: String
) {
    var nameLocalizations: Map<Locale, String>? = null
    var descriptionLocalizations: Map<Locale, String>? = null
    // Groups can't have executors!
    val subcommands = mutableListOf<SlashCommandDeclarationBuilder>()

    fun subcommand(name: String, description: String, block: SlashCommandDeclarationBuilder.() -> (Unit)) {
        subcommands += SlashCommandDeclarationBuilder(name, description).apply(block)
    }

    fun build(): SlashCommandGroupDeclaration {
        return InteraKTionsSlashCommandGroupDeclaration(
            name,
            nameLocalizations,
            description,
            descriptionLocalizations,
            subcommands.map { it.build() }
        )
    }
}

// ===[ USER COMMANDS ]===
fun userCommand(name: String, executor: UserCommandExecutor) = UserCommandDeclarationBuilder(name, executor)

@InteraKTionsDslMarker
class UserCommandDeclarationBuilder(val name: String, val executor: UserCommandExecutor) {
    var nameLocalizations: Map<Locale, String>? = null
    var defaultMemberPermissions: Permissions? = null
    var dmPermission: Boolean? = null

    fun build(): UserCommandDeclaration {
        return InteraKTionsUserCommandDeclaration(
            name,
            nameLocalizations,
            executor,
            defaultMemberPermissions,
            dmPermission
        )
    }
}

// ===[ MESSAGE COMMANDS ]===
fun messageCommand(name: String, executor: MessageCommandExecutor) = MessageCommandDeclarationBuilder(name, executor)

@InteraKTionsDslMarker
class MessageCommandDeclarationBuilder(val name: String, val executor: MessageCommandExecutor) {
    var nameLocalizations: Map<Locale, String>? = null
    var defaultMemberPermissions: Permissions? = null
    var dmPermission: Boolean? = null

    fun build(): MessageCommandDeclaration {
        return InteraKTionsMessageCommandDeclaration(
            name,
            nameLocalizations,
            executor,
            defaultMemberPermissions,
            dmPermission
        )
    }
}
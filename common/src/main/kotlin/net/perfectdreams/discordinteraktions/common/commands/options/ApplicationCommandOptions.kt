package net.perfectdreams.discordinteraktions.common.commands.options

import dev.kord.common.entity.*
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordChannel
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordRole
import net.perfectdreams.discordinteraktions.platforms.kord.entities.KordUser

open class ApplicationCommandOptions {
    companion object {
        val NO_OPTIONS = object : ApplicationCommandOptions() {}
    }

    val arguments = mutableListOf<CommandOption<*>>()

    fun string(
        name: String,
        description: String,
        builder: StringCommandOptionBuilder.() -> (Unit) = {}
    ): StringCommandOption<String> = StringCommandOption<String>(name, description, true).apply(builder).also {
        it.register()
    }

    fun optionalString(
        name: String,
        description: String,
        builder: StringCommandOptionBuilder.() -> (Unit) = {}
    ): StringCommandOption<String?> = StringCommandOption<String?>(name, description, false).apply(builder).also {
        it.register()
    }

    fun integer(
        name: String,
        description: String,
        builder: IntegerCommandOptionBuilder.() -> (Unit) = {}
    ): IntegerCommandOption<Long> = IntegerCommandOption<Long>(name, description, true).apply(builder).also {
        it.register()
    }

    fun optionalInteger(
        name: String,
        description: String,
        builder: IntegerCommandOptionBuilder.() -> (Unit) = {}
    ): IntegerCommandOption<Long?> = IntegerCommandOption<Long?>(name, description, false).apply(builder).also {
        it.register()
    }

    fun number(
        name: String,
        description: String,
        builder: NumberCommandOptionBuilder.() -> (Unit) = {}
    ): NumberCommandOption<Double> = NumberCommandOption<Double>(name, description, true).apply(builder).also {
        it.register()
    }

    fun optionalNumber(
        name: String,
        description: String,
        builder: NumberCommandOptionBuilder.() -> (Unit) = {}
    ): NumberCommandOption<Double?> = NumberCommandOption<Double?>(name, description, false).apply(builder).also {
        it.register()
    }

    fun boolean(
        name: String,
        description: String,
        builder: BooleanCommandOptionBuilder.() -> (Unit) = {}
    ): BooleanCommandOption<Boolean> = BooleanCommandOption<Boolean>(name, description, true).apply(builder).also {
        it.register()
    }

    fun optionalBoolean(
        name: String,
        description: String,
        builder: BooleanCommandOptionBuilder.() -> (Unit) = {}
    ): BooleanCommandOption<Boolean?> = BooleanCommandOption<Boolean?>(name, description, false).apply(builder).also {
        it.register()
    }

    fun user(
        name: String,
        description: String,
        builder: UserCommandOptionBuilder.() -> (Unit) = {}
    ): UserCommandOption<KordUser> = UserCommandOption<KordUser>(name, description, true).apply(builder).also {
        it.register()
    }

    fun optionalUser(
        name: String,
        description: String,
        builder: UserCommandOptionBuilder.() -> (Unit) = {}
    ): UserCommandOption<KordUser?> =
        UserCommandOption<KordUser?>(name, description, false).apply(builder).also {
            it.register()
        }

    fun role(
        name: String,
        description: String,
        builder: RoleCommandOptionBuilder.() -> (Unit) = {}
    ): RoleCommandOption<KordRole> = RoleCommandOption<KordRole>(name, description, true).apply(builder).also {
        it.register()
    }

    fun optionalRole(
        name: String,
        description: String,
        builder: RoleCommandOptionBuilder.() -> (Unit) = {}
    ): RoleCommandOption<KordRole?> = RoleCommandOption<KordRole?>(name, description, false).apply(builder).also {
        it.register()
    }

    fun channel(
        name: String,
        description: String,
        builder: ChannelCommandOptionBuilder.() -> (Unit) = {}
    ): ChannelCommandOption<KordChannel> =
        ChannelCommandOption<KordChannel>(name, description, true).apply(builder).also {
            it.register()
        }

    fun optionalChannel(
        name: String,
        description: String,
        builder: ChannelCommandOptionBuilder.() -> (Unit) = {}
    ): ChannelCommandOption<KordChannel?> =
        ChannelCommandOption<KordChannel?>(name, description, false).apply(builder).also {
            it.register()
        }

    fun mentionable(
        name: String,
        description: String,
        builder: MentionableCommandOptionBuilder.() -> (Unit) = {}
    ): MentionableCommandOption<CommandArgument.MentionableArgument> =
        MentionableCommandOption<CommandArgument.MentionableArgument>(name, description, true).apply(builder).also {
            it.register()
        }

    fun optionalMentionable(
        name: String,
        description: String,
        builder: MentionableCommandOptionBuilder.() -> (Unit) = {}
    ): MentionableCommandOption<CommandArgument.MentionableArgument?> =
        MentionableCommandOption<CommandArgument.MentionableArgument?>(name, description, false).apply(builder).also {
            it.register()
        }

    fun attachment(
        name: String,
        description: String,
        builder: AttachmentCommandOptionBuilder.() -> (Unit) = {}
    ): AttachmentCommandOption<DiscordAttachment> =
        AttachmentCommandOption<DiscordAttachment>(name, description, true).apply(builder).also {
            it.register()
        }

    fun optionalAttachment(
        name: String,
        description: String,
        builder: AttachmentCommandOptionBuilder.() -> (Unit) = {}
    ): AttachmentCommandOption<DiscordAttachment?> =
        AttachmentCommandOption<DiscordAttachment?>(name, description, false).apply(builder).also {
            it.register()
        }

    private fun <T> CommandOption<T>.register(): CommandOption<T> {
        if (arguments.any { it.name == this.name })
            throw IllegalArgumentException("Duplicate argument \"${this.name}\"!")

        arguments.add(this)
        return this
    }
}
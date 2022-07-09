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
        builder: StringOptionBuilder.() -> (Unit) = {}
    ): StringOption<String> = StringOption<String>(name, description, true).apply(builder).also {
        it.register()
    }

    fun optionalString(
        name: String,
        description: String,
        builder: StringOptionBuilder.() -> (Unit) = {}
    ): StringOption<String?> = StringOption<String?>(name, description, false).apply(builder).also {
        it.register()
    }

    fun long(
        name: String,
        description: String,
        builder: LongOptionBuilder.() -> (Unit) = {}
    ): LongOption<Long> = LongOption<Long>(name, description, true).apply(builder).also {
        it.register()
    }

    fun optionalLong(
        name: String,
        description: String,
        builder: LongOptionBuilder.() -> (Unit) = {}
    ): LongOption<Long?> = LongOption<Long?>(name, description, false).apply(builder).also {
        it.register()
    }

    fun double(
        name: String,
        description: String,
        builder: DoubleOptionBuilder.() -> (Unit) = {}
    ): DoubleOption<Double> = DoubleOption<Double>(name, description, true).apply(builder).also {
        it.register()
    }

    fun optionalDouble(
        name: String,
        description: String,
        builder: DoubleOptionBuilder.() -> (Unit) = {}
    ): DoubleOption<Double?> = DoubleOption<Double?>(name, description, false).apply(builder).also {
        it.register()
    }

    fun boolean(
        name: String,
        description: String,
        builder: BooleanOptionBuilder.() -> (Unit) = {}
    ): BooleanOption<Boolean> = BooleanOption<Boolean>(name, description, true).apply(builder).also {
        it.register()
    }

    fun optionalBoolean(
        name: String,
        description: String,
        builder: BooleanOptionBuilder.() -> (Unit) = {}
    ): BooleanOption<Boolean?> = BooleanOption<Boolean?>(name, description, false).apply(builder).also {
        it.register()
    }

    fun user(
        name: String,
        description: String,
        builder: UserOptionBuilder.() -> (Unit) = {}
    ): UserOption<KordUser> = UserOption<KordUser>(name, description, true).apply(builder).also {
        it.register()
    }

    fun optionalUser(
        name: String,
        description: String,
        builder: UserOptionBuilder.() -> (Unit) = {}
    ): UserOption<KordUser?> =
        UserOption<KordUser?>(name, description, false).apply(builder).also {
            it.register()
        }

    fun role(
        name: String,
        description: String,
        builder: RoleOptionBuilder.() -> (Unit) = {}
    ): RoleOption<KordRole> = RoleOption<KordRole>(name, description, true).apply(builder).also {
        it.register()
    }

    fun optionalRole(
        name: String,
        description: String,
        builder: RoleOptionBuilder.() -> (Unit) = {}
    ): RoleOption<KordRole?> = RoleOption<KordRole?>(name, description, false).apply(builder).also {
        it.register()
    }

    fun channel(
        name: String,
        description: String,
        builder: ChannelOptionBuilder.() -> (Unit) = {}
    ): ChannelOption<KordChannel> =
        ChannelOption<KordChannel>(name, description, true).apply(builder).also {
            it.register()
        }

    fun optionalChannel(
        name: String,
        description: String,
        builder: ChannelOptionBuilder.() -> (Unit) = {}
    ): ChannelOption<KordChannel?> =
        ChannelOption<KordChannel?>(name, description, false).apply(builder).also {
            it.register()
        }

    fun mentionable(
        name: String,
        description: String,
        builder: MentionableOptionBuilder.() -> (Unit) = {}
    ): MentionableOption<CommandArgument.MentionableArgument> =
        MentionableOption<CommandArgument.MentionableArgument>(name, description, true).apply(builder).also {
            it.register()
        }

    fun optionalMentionable(
        name: String,
        description: String,
        builder: MentionableOptionBuilder.() -> (Unit) = {}
    ): MentionableOption<CommandArgument.MentionableArgument?> =
        MentionableOption<CommandArgument.MentionableArgument?>(name, description, false).apply(builder).also {
            it.register()
        }

    fun attachment(
        name: String,
        description: String,
        builder: AttachmentOptionBuilder.() -> (Unit) = {}
    ): AttachmentOption<DiscordAttachment> =
        AttachmentOption<DiscordAttachment>(name, description, true).apply(builder).also {
            it.register()
        }

    fun optionalAttachment(
        name: String,
        description: String,
        builder: AttachmentOptionBuilder.() -> (Unit) = {}
    ): AttachmentOption<DiscordAttachment?> =
        AttachmentOption<DiscordAttachment?>(name, description, false).apply(builder).also {
            it.register()
        }

    private fun <T> CommandOption<T>.register(): CommandOption<T> {
        if (arguments.any { it.name == this.name })
            throw IllegalArgumentException("Duplicate argument \"${this.name}\"!")

        arguments.add(this)
        return this
    }
}
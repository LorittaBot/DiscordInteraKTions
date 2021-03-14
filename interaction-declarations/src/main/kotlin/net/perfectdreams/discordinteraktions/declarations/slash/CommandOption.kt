package net.perfectdreams.discordinteraktions.declarations.slash

import net.perfectdreams.discordinteraktions.api.entities.Channel
import net.perfectdreams.discordinteraktions.api.entities.Role
import net.perfectdreams.discordinteraktions.api.entities.User

open class CommandOption<T>(
    val type: Int,
    val name: String,
    val description: String,
    val required: Boolean,
    val choices: List<CommandChoice>
)

// ===[ REQUIRED ]===
// The reason it is all extension methods is because there isn't a way to convert a "<T?>" to a non nullable "<T>"
// And all of them have "@JvmName" even tho we don't support Java is due to type name clash
@JvmName("requiredString?")
fun CommandOption<String?>.required() = CommandOption<String>(
    type,
    name,
    description,
    true,
    choices
)

@JvmName("requiredInt?")
fun CommandOption<Int?>.required() = CommandOption<Int>(
    type,
    name,
    description,
    true,
    choices
)

@JvmName("requiredBoolean?")
fun CommandOption<Boolean?>.required() = CommandOption<Boolean>(
    type,
    name,
    description,
    true,
    choices
)

@JvmName("requiredUser?")
fun CommandOption<User?>.required() = CommandOption<User>(
    type,
    name,
    description,
    true,
    choices
)

@JvmName("requiredChannel?")
fun CommandOption<Channel?>.required() = CommandOption<Channel>(
    type,
    name,
    description,
    true,
    choices
)

@JvmName("requiredRole?")
fun CommandOption<Role?>.required() = CommandOption<Role>(
    type,
    name,
    description,
    true,
    choices
)

// ===[ CHOICE ]===
@JvmName("choiceString")
fun CommandOption<String>.choice(value: String, name: String) = CommandOption<String?>(
    type,
    this.name,
    description,
    true,
    choices + StringCommandChoice(name, value)
)

@JvmName("choiceString?")
fun CommandOption<String?>.choice(value: String, name: String) = CommandOption<String?>(
    type,
    this.name,
    description,
    true,
    choices + StringCommandChoice(name, value)
)

@JvmName("choiceInt")
fun CommandOption<Int>.choice(value: Int, name: String) = CommandOption<Int>(
    type,
    this.name,
    description,
    true,
    choices + IntegerCommandChoice(name, value)
)


@JvmName("choiceInt?")
fun CommandOption<Int?>.choice(value: Int, name: String) = CommandOption<Int>(
    type,
    this.name,
    description,
    true,
    choices + IntegerCommandChoice(name, value)
)
<h1 align="center">👩‍💻 Discord InteraKTions 👩‍💻</h1>

## 😭 This project is deprecated!

Discord InteraKTions was created for my own bot, [Loritta](https://github.com/LorittaBot/Loritta), to make creating slash commands over HTTP with Kord a breeze.

However, after heavily using Kord in production, and encountering a lot of issues that needed patches and modifications to Kord's source code, I've decided to migrate back to JDA until Kord is mature and battle tested enough. While I *love* Kord, I also *love* not banging my head against the wall trying to figure hard to debug and to reproduce issues.

Originally my plan to migrate to Kord was to allow low downtime restarts for my bot, and during one of my "banging my head against the wall" sessions, I thought about forking JDA to support session checkpoints + gateway resume after a restart. After all, I was already forking Kord anyway, so why not try to implement it in JDA?

And that's what happened. I spent ~2 months trying to migrate Loritta to Kord, ended up replacing [everything with something that I spent ~5 days making](https://github.com/LorittaBot/DeviousJDA/blob/master/src/examples/java/SessionCheckpointAndGatewayResumeExample.kt) (heck, it was even less than 5 days, I made the first working proof of concept in only a few hours!).

Maybe in the future I will create a public "spiritual successor" to Discord InteraKTions. But for now, that spiritual successor lives in Loritta's source code, while I thinker and play around with how to improve the current Discord InteraKTions design. 😊


**🚧 Experiemental Project 🚧** / **Not finished yet so you shouldn't use it!!**

Discord InteraKTions allows you to create, receive and process [Discord's Application Commands](https://discord.com/developers/docs/interactions/application-commands) and [Message Components](https://discord.com/developers/docs/interactions/message-components) via a HTTP Web Server or via the Gateway. Built on top of [Kord](https://github.com/kordlib/kord), using interactions is easy and fun!

Discord InteraKTions is as barebones as it gets compared to other libs like [Kord Extensions](https://github.com/Kord-Extensions/kord-extensions), and this is intentional! While Discord InteraKTions does provide an easy way to create slash command declarations and slash command executors, the rest (dependency injection, paginator, translations, etc) is up to you to do it your own way!

## 📝 Status of Discord InteraKTions

While Discord InteraKTions has a bunch of nifty features, it still doesn't support everything! Keep in mind that Discord InteraKTions is still in active development and every commit is a new chance of breaking your code due to unnecessary refactors. Heck, even the examples below may be already out of date due to changes in the code! 😛

* [X] Receiving Discord Interactions via Web Server/Webhooks
* [X] Validating Discord Interactions via Web Server/Webhooks
* [X] Processing Discord Interactions/Slash Commands
* [X] Sending Messages
* [X] Sending Messages Directly on Discord's POST Request
* [X] Sending Follow Up Messages via REST
* [X] Registering Slash Commands on Discord
* [X] Subcommands and Subcommand Groups
* [X] Sending Embeds
* [X] Abstractions On Top of Kord
* [X] User/Message Commands (Context Menu)
* [X] Buttons
* [X] Select Menus
* [X] Autocomplete
* [X] Permissions
* [X] *(Experimental)* Modals
* [ ] *Good* Documentation
* [ ] Being a good project :3

## 🤔 How it Works

Discord InteraKTions works on top of Kord. Your code also doesn't care if it is receiving interactions over HTTP or over the gateway, it is all the same thing for them!

All of the "dirty work", like interaction request validation and parsing, is already done for you, so you only need to care about creating your nifty slash commands and having fun!

Discord InteraKTions' Web Server and Gateway modules uses [Kord](https://github.com/kordlib/kord)'s `common` and `rest` modules for data serialization and REST interactions, so keep that in mind if you are already worked with Kord `core`, because Discord InteraKTions does **not** use `core` classes!

### 📘 Examples

First, let's see how a command looks like, just so you can get a feel on how you will be using Discord InteraKTions when developing your commands. Don't worry, we will explain how everything works, with more detailed examples down the road. In this example, we will use the Web Server backend.

```kotlin
suspend fun main() {
    val interactionsServer = InteractionsServer(
        Snowflake(12345L), // Your application ID, get it from the Discord Developers' dashboard!
        "application_public_key_here", // The application's public key, get it from the Discord Developers' dashboard!
        "application_token_here", // The application's token, get it from the Discord Developers' dashboard!
        12212 // The webserver port
    )

    // Register the command...
    // Keep in mind that you need to register all the executors used in the declarations!
    interactionsServer.commandManager.register(
        CharacterCommand,   // The declaration of the command
    )

    // And now we will create our command registry to register our commands!
    val registry = KordCommandRegistry(
        Snowflake(12345L), // Your application ID, get it from the Discord Developers' dashboard!
        interactionsServer.rest,
        interactionsServer.commandManager
    )

    // And now update all commands registered in our command manager!
    registry.updateAllCommandsInGuild(
        Snowflake(40028922L), // Change to your Guild ID
        // This compares the currently registered commands on Discord with the commands in the Command Manager
        // If a command is missing from the Command Manager but is present on Discord, it is deleted from Discord!
        true
    )

    interactionsServer.start() // This starts the interactions web server on port 12212!

    // Now we are live! Set your interaction URL on Discord's Developer Portal and have fun!
    //
    // Don't forget that your Web Server should be accessible from the outside world!
    // If you are doing this for tests & stuff, you can use ngrok or a SSH Reverse Tunnel
}

// This is the slash command declaration, this is used when registering the command on Discord
//
// The reason it is a companion object is to allow you to register the command on Discord without
// needing to initialize the command class! (which can be a *pain* if your command requires dependency injection)
object CharacterCommand : SlashCommandDeclarationWrapper {
    override fun declaration() = slashCommand(
        "character", // The command label
        "So many choices, so little time..." // The command description shown in the Discord UI
    ) {
        executor = CharacterExecutor() // This is a reference to what executor should execute the command
    }
}

class CharacterExecutor : SlashCommandExecutor() {
    inner class Options : ApplicationCommandOptions() {
        val character = string("character", "Select a Character!") { // Here we are creating a String option
            choice("loritta", "Loritta Morenitta :3") // ...with custom choices! 
            choice("pantufa", "Pantufa ;w;")
            choice("gabriela", "Gabriela ^-^")
        }

        val repeat = optionalLong("repeat", "How many times the character name should be repeated") // Here we are creating a Int option
    }

    override suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments) {
        val character = args[options.character] // This is a "String" because the option is required
        val repeatCount = args[options.repeat] ?: 1 // This is a "Int?" because the option is not required (optional)

        val characterName = when (character) {
            "loritta" -> "Loritta Morenitta"
            "pantufa" -> "Pantufa"
            "gabriela" -> "Gabriela"
            else -> throw IllegalArgumentException("I don't know how to handle $character!")
        }

        val builder = StringBuilder("You selected... ")
        repeat(repeatCount) {
            builder.append(characterName)
            builder.append(' ')
        }

        context.sendMessage {
            // Sends a message
            //
            // Because we use Web Servers for responses, this response will be replied directly on Discord's POST request!
            // This has the advantage of not consuming any rate limits, which is pretty nifty!
            //
            // If you send multiple messages, the first message will be replied directly on Discord's POST request and the rest of them will
            // be sent as follow up messages using Discord's REST API
            content = builder.toString()
        }
    }
}
```

Too much happening, eh? Don't worry, now let's see things in a bite size manner.

Discord Interactions has a very nice thing that you can reply to a interaction directly on Discord's POST request, but you can also defer the message (if you are going to take more than three seconds to process it) and/or send follow up messages. Discord InteraKTions automatically handles deferring and message follow ups via REST, so if your command is like this:

```kotlin
override suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments) {
    context.sendMessage {
        content = "Hello World! Loritta is very cute!! :3"
    }
}
```

If you already used interactions before, you will notice that there isn't `defer` call of any kind here, this is because Discord InteraKTions knows that the request wasn't deferred and then responds the interaction with the `Create Interaction Response` API call! On a webserver backend, the request is replied directly on Discord's POST request, avoiding consuming rate limits and sending HTTP requests to Discord!

But if your command is like this...

```kotlin
override suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments) {
    context.sendMessage {
        content = "Hello World! Loritta is very cute!! :3"
    }

    context.sendMessage {
        content = "Bye World! Pantufa and Gabriela are also very cute!! :3"
    }
}
```

The first `sendMessage` will be processed as a  `Create Interaction Response`, while the second `sendMessage` will be processed as a follow up message using the `Create Followup Message` API call.

Of course, Discord InteraKTions does not magically know how long your tasks take to be finished (example: image processing, pulling stuff from a external slow API, etc), because Discord has a max 3 second limit to send a interaction response, so in those cases, you need to handle deferring yourself to avoid a "Interaction Failed"!

```kotlin
override suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments) {
    context.deferChannelMessage() // Defer the message, we will follow up later

    delay(10_000) // Very slow task here

    context.sendMessage {
        content = "We searched everywhere on the world, and we found out that Loritta is still very cute!! :3"
    }
}
```

In this case, the interaction will be deferred and then later edited with the `Edit Original Interaction Response` API call.

You can also upload files with Discord InteraKTions!

```kotlin
override suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments) {
    context.sendMessage {
        content = "haha funni image"

        addFile("image.png", File("/path/to/file/image.png").inputStream())
    }
}
```

If you already used interactions before, you know that you can't send images in the `Create Interaction Response` API call, so in this case, Discord InteraKTions will automatically defer and then use the `Edit Original Interaction Response` call!

Check out our [sample bot](/sample/src/main/kotlin/com/mrpowergamerbr/nicolebot) to learn more examples!

### 🛠️ Installation

First, add the PerfectDreams repository to your project

```kotlin
repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots") // Required by Kord
    maven("https://repo.perfectdreams.net/")
}
```

After that, you have two options on how to process and handle the interactions...

#### Interactions via HTTP POST

##### Kord and Ktor

Add the Kord Web Server via Ktor Support module to your project

```kotlin
dependencies {
    ...
    implementation("net.perfectdreams.discordinteraktions:webserver-ktor-kord:0.0.14-SNAPSHOT") // Check latest version in https://github.com/LorittaBot/DiscordInteraKTions/blob/main/settings.gradle.kts#L15
    ...
}
```

```kotlin
val interactionsServer = InteractionsServer(
    12345L, // Your application ID, get it from the Discord Developers' dashboard!
    "application_public_key_here", // The application's public key, get it from the Discord Developers' dashboard!
    "application_token_here", // The application's token, get it from the Discord Developers' dashboard!
    12212 // The webserver port
)

// Register the command...
// Keep in mind that you need to register all the executors used in the declarations!
interactionsServer.commandManager.register(
    CharacterCommand()
)

// And now we will create our command registry!
val registry = KordCommandRegistry(
    Snowflake(12345L), // Your application ID, get it from the Discord Developers' dashboard!
    interactionsServer.rest,
    interactionsServer.commandManager
)

// And now update all commands registered in our command manager!
registry.updateAllCommandsInGuild(
    Snowflake(40028922L), // Change to your Guild ID
    // This compares the currently registered commands on Discord with the commands in the Command Manager
    // If a command is missing from the Command Manager but is present on Discord, it is deleted from Discord!
    true
)

interactionsServer.start() // This starts the interactions web server on port 12212!

// Now we are live! Set your interaction URL on Discord's Developer Portal and have fun!
//
// Don't forget that your Web Server should be accessible from the outside world!
// If you are doing this for tests & stuff, you can use ngrok or a SSH Reverse Tunnel
```

#### Interactions via the Gateway

##### Kord

Add the Kord Gateway Support module to your project

```kotlin
dependencies {
    ...
    implementation("net.perfectdreams.discordinteraktions:gateway-kord:0.0.14-SNAPSHOT") // Check latest version in https://github.com/LorittaBot/DiscordInteraKTions/blob/main/settings.gradle.kts#L15
    ...
}
```

```kotlin
suspend fun main() {
    val applicationId = Snowflake(12345L) // Change the Application ID to your Bot's Application ID
    val client = Kord("bot_token_here")

    val commandManager = CommandManager()

    // Register the command...
    // Keep in mind that you need to register all the executors used in the declarations!
    commandManager.register(
        CharacterCommand()
    )

    // And now we will create our command registry!
    val registry = KordCommandRegistry(
        Snowflake(12345L), // Your application ID, get it from the Discord Developers' dashboard!
        client.rest,
        commandManager
    )

    // And now update all commands registered in our command manager!
    registry.updateAllCommandsInGuild(
        Snowflake(40028922L), // Change to your Guild ID
        // This compares the currently registered commands on Discord with the commands in the Command Manager
        // If a command is missing from the Command Manager but is present on Discord, it is deleted from Discord!
        true
    )

    client.gateway.gateways.forEach {
        it.value.installDiscordInteraKTions( // We will install the Discord InteraKTions listener on every gateway
            Snowflake(12345L), // Your application ID, get it from the Discord Developers' dashboard!
            client.rest,
            commandManager
        )
    }
    
    client.login()
}
```

## 📦 Modules

### `common`

Contains Discord InteraKTions' API and other nifty stuff.

### `interaction-declarations`

Contains the classes related to declaration of slash commands.

### `platforms:common-kord`

Common classes used for Kord platforms.

### `platforms:gateway-kord`

Implementation of the Discord InteraKTions' API using Kord, running interactions over the gateway.

### `platforms:webserver-ktor-kord`

Implementation of the Discord InteraKTions' API using Kord, running interactions over HTTP with a [Ktor](https://ktor.io/) Web Server.

### `requests-verifier`

Contains the code used to verify Discord requests, useful if you want to create your own way to process Discord requests with your own Web Server!

```kotlin
val keyVerifier = InteractionRequestVerifier(publicKey)

...

val signature = call.request.header("X-Signature-Ed25519")!!
val timestamp = call.request.header("X-Signature-Timestamp")!!

val verified = keyVerifier.verifyKey(
    text,
    signature,
    timestamp
)

if (!verified) {
    // Request is not valid, oh no...
    call.respondText("", ContentType.Application.Json, HttpStatusCode.Unauthorized)
    return
}

// Request is valid, yay!
```

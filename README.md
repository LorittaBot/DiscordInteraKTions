# Discord InteraKTions

**Not finished yet so you shouldn't use it yet!!**

Discord InteraKTions allows you to create, receive and process [Discord's Slash Commands](https://discord.com/developers/docs/interactions/slash-commands) via a HTTP Web Server. Built on top of [Kord](https://github.com/kordlib/kord), using interactions is easy and fun!

## Status of Discord InteraKTions

Nothing works yet so you shouldn't use it!! (Okay *technically* sending messages already work)

* [X] Receiving Discord Interactions via Web Server/Webhooks
* [X] Validating Discord Interactions via Web Server/Webhooks
* [X] Processing Discord Interactions/Slash Commands
* [X] Sending Messages
* [X] Sending Messages Directly on Discord's POST Request
* [X] Sending Follow Up Messages via REST
* [X] Registering Slash Commands on Discord
* [X] Subcommands and Subcommand Groups
* [ ] Sending Embeds
* [ ] Abstractions On Top of Kord
* [ ] *Good* Documentation
* [ ] Being a good project :3

## How it Works

Discord InteraKTions uses a Web Server (with [Ktor](https://ktor.io/)) ready to receive interactions from Discord! All of the "dirty work", like interaction request validation and parsing, is already done for you, so you only need to care about creating your nifty slash commands and having fun!

Discord InteraKTions uses [Kord](https://github.com/kordlib/kord)'s `common` and `rest` modules for data serialization and REST interactions, keep in mind that Discord InteraKTions creates a abstraction layer between Discord InteraKTions and Kord, to avoid interacting *directly* with Kord's 1:1 Discord mappings. (Which is a good and ver cool thing that Kord works like that! You can create your abstractions on top of Kord very easily!)

Discord Interactions has a very nice thing that you can reply to a interaction directly on Discord's POST request, but you can also defer the message (if you are going to take more than three seconds to process it) and/or send follow up messages. Discord InteraKTions automatically handles deferring and message follow ups via REST, so if your command is like this:

```kotlin
override suspend fun executes(context: SlashCommandContext) {
    context.sendMessage {
        content = "Hello World! Loritta is very cute!! :3"
    }
}
```

Discord InteraKTions will handle this as "reply directly on Discord's POST request", which is nice because you avoid consuming rate limits and is pretty useful if your command won't take more than three seconds to process.

But if your command is like this
```kotlin
override suspend fun executes(context: SlashCommandContext) {
    context.sendMessage {
        content = "Hello World! Loritta is very cute!! :3"
    }

    context.sendMessage {
        content = "Bye World! Pantufa and Gabriela are also very cute!! :3"
    }
}
```
The first `sendMessage` will be processed as a "reply directly on Discord's POST request" while the second one will be sent as a follow up message via REST.

You can of course handle deferring yourself, if you already know that your command replies will need to be deferred due to your processing taking too long (example: image processing, pulling stuff from a external slow API, etc)
```kotlin
override suspend fun executes(context: SlashCommandContext) {
    context.defer() // Defer the message, we will follow up later

    delay(10_000) // Very slow task here

    context.sendMessage {
        content = "We searched everywhere on the world, and we found out that Loritta is still very cute!! :3"
    }
}
```

## Modules

### `core`

Contains the web server and other nifty stuff.

### `interaction-declarations`

Contains the classes related to declaration of slash commands.

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

## How to Use

### Installation

First, add the PerfectDreams repository to your project

```kotlin
repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots") // Required by Kord
    maven("https://repo.perfectdreams.net/")
}
```

Then add the Discord InteraKTions dependency!

```kotlin
dependencies {
    ...
    implementation("net.perfectdreams.discordinteraktions:core:0.0.3-SNAPSHOT")
    ...
}
```

### Creating Slash Commands

First we need to create a Slash Command, here's a example of how you can create your own

```kotlin
class CharacterCommand : SlashCommand(CharacterCommand) {
    // This is the slash command declaration, this is used when registering the command on Discord
    //
    // The reason it is a companion object is to allow you to register the command on Discord without
    // needing to initialize the command class! (which can be a *pain* if your command requires dependency injection)

    companion object : SlashCommandDeclaration(
        name = "character", // The command label
        description = "So many choices, so little time..." // The command description shown in the Discord UI
    ) {
        // By default, if you don't override the options, no options will be set
        override val options = Options

        object Options : SlashCommandDeclaration.Options() {
            val character = string("character", "Select a Character!") // Here we are creating a String option
                .required() // ...and it is required
                .choice("loritta", "Loritta Morenitta :3") // ...with custom choices!
                .choice("pantufa", "Pantufa ;w;")
                .choice("gabriela", "Gabriela ^-^")
                .register() // Don't forget to register!

            val repeat = integer("repeat", "How many times the character name should be repeated") // Here we are creating a Int option
                .register() // This isn't required (so it is optional!) and, as always, don't forget to register!
        }
    }

    override suspend fun executes(context: SlashCommandContext) {
        val character = options.character.get(context) // This is a "String" because the option is required
        val repeatCount = options.repeat.get(context) ?: 1 // This is a "Int?" because the option is not required (optional)

        val characterName = when (character) {
            "loritta" -> "Loritta Morenitta"
            "pantufa" -> "Pantufa"
            "gabriela" -> "Gabriela"
            else -> throw IllegalArgumentException("I don't know how to handle $character!")
        }

        val builder = StringBuilder("You selected... ")
        repeat(repeatCount) {
            builder.append(characterName)
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

After that, you have two options on how to process and handle the interactions...

#### Interactions via HTTP POST

```kotlin
val interactions = InteractionsServer(
    applicationId = 12345L, // Change the Application ID to your Bot's Application ID
    publicKey = "bot_public_key_here",
    token = "bot_token_here"
)

// Register the command...
interactions.commandManager.register(CharacterCommand())

// And now register all commands registered in our command manager!
interactions.commandManager.updateAllCommandsInGuild(
    Snowflake(40028922L), // Change to your Guild ID
    // This compares the currently registered commands on Discord with the commands in the Command Manager
    // If a command is missing from the Command Manager but is present on Discord, it is deleted from Discord!
    deleteUnknownCommands = true
)

interactions.start() // This starts the interactions web server on port 12212!

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
    implementation("net.perfectdreams.discordinteraktions:gateway-kord:0.0.3-SNAPSHOT")
    ...
}
```

```kotlin
suspend fun main() {
    val applicationId = Snowflake(12345L) // Change the Application ID to your Bot's Application ID
    val client = Kord("bot_token_here")

    val commandManager = CommandManager(
        client.rest,
        applicationId
    )

    commandManager.register(CharacterCommand())

    // And now register all commands registered in our command manager!
    commandManager.updateAllCommandsInGuild(
        Snowflake(40028922L), // Change to your Guild ID
        // This compares the currently registered commands on Discord with the commands in the Command Manager
        // If a command is missing from the Command Manager but is present on Discord, it is deleted from Discord!
        deleteUnknownCommands = true
    )

    client.gateway.gateways.forEach {
        it.value.installDiscordInteraKTions( // We will install the Discord InteraKTions listener on every gateway
            commandManager
        )
    }
    
    client.login()
}
```
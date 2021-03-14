# Discord InteraKTions

**Not finished yet so you shouldn't use it yet!!**

Discord InteraKTions allows you to create, receive and process [Discord's Slash Commands](https://discord.com/developers/docs/interactions/slash-commands) via a HTTP Web Server. Built on top of [Kord](https://github.com/kordlib/kord), using interactions is easy and fun!

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

## Status of Discord InteraKTions

Nothing works yet so you shouldn't use it!! (Okay *technically* sending messages already work)

## How to Use

```kotlin
class CharacterCommand : SlashCommand(CharacterCommand) {
    companion object : SlashCommandDeclaration() {
        override val name = "character"
        override val description = "So many choices, so little time..."

        override val options = Options

        object Options : SlashCommandDeclaration.Options() {
            val character = string("character", "Select a Character!")
                .required()
                .choice("loritta", "Loritta Morenitta \uD83D\uDE18")
                .choice("pantufa", "Pantufa")
                .choice("gabriela", "Gabriela")
                .register()

            val repeat = integer("repeat", "How many times the character name should be repeated")
                .register()
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
            content = builder.toString()
        }
    }
}
```

```kotlin
val interactions = InteractionsServer(
    applicationId = 12345L, // Change the Application ID to your Bot's Application ID
    publicKey = "bot_public_key_here",
    token = "bot_token_here"
)

interactions.commandManager.commands.add(CharacterCommand())

interactions.start() // This starts the interactions web server
```
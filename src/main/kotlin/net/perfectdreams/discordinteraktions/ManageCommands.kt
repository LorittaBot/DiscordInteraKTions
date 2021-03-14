package net.perfectdreams.discordinteraktions

/* import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.*
import net.perfectdreams.pantufa.PantufaBot
import net.perfectdreams.pantufa.interactions.commands.*
import net.perfectdreams.pantufa.utils.PantufaConfig

fun main() {
    val pantufa = PantufaBot(
        PantufaConfig(
            "a",
            PantufaConfig.PostgreSqlConfig("0", 0, "0", "0"),
            PantufaConfig.PostgreSqlConfig("0", 0, "0", "0")
        )
    )

    val token = "MzkwOTI3ODIxOTk3OTk4MDgx.WjK95w.gSmXJRfqasatLgSASVy0ShmjZoU"

    val commands = mutableListOf(
        LSXCommand(pantufa),
        PingCommand(pantufa),
        RegistrarCommand(pantufa),
        OnlineCommand(pantufa),
        MoneyCommand(pantufa),
        PesadelosCommand(pantufa),
        ChatColorCommand(pantufa),
        VIPInfoCommand(pantufa),
        MinecraftUserCommand(pantufa)
    )

    for (command in commands) {
        val data = buildJsonObject {
            put("name", command.label)
            put("description", command.description)

            putJsonArray("options") {
                command.arguments.forEach {
                    addJsonObject {
                        put("name", it.name)
                        put("description", it.description)
                        put("type", it.type)
                        put("required", it.required)

                        if (it.choices.isNotEmpty()) {
                            putJsonArray("choices") {
                                for (choice in it.choices) {
                                    addJsonObject {
                                        put("name", choice.name)
                                        put("value", choice.value)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        println(data)

        val http = HttpClient { expectSuccess = false }

        runBlocking {
            val response =
                http.post<HttpResponse>("https://discord.com/api/v8/applications/390927821997998081/commands") {
                    header("Authorization", "Bot $token")
                    contentType(ContentType.Application.Json)
                    body = data.toString()
                }

            val headers = response.headers
            val remaining = headers["x-ratelimit-remaining"]

            println(response.status)
            println(response.headers)

            if (remaining == "0") {
                println("Will get ratelimited, let's wait...")
                Thread.sleep((headers["x-ratelimit-reset-after"]!!.toDouble() * 1000).toLong())
            }
        }
    }
} */
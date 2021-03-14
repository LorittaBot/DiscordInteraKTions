package net.perfectdreams.discordinteraktions.context

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import mu.KotlinLogging
import net.perfectdreams.discordinteraktions.InteractionsServer
import net.perfectdreams.discordinteraktions.entities.OriginalMessage
import net.perfectdreams.discordinteraktions.entities.Message
import net.perfectdreams.discordinteraktions.entities.MessageToBeSent
import net.perfectdreams.discordinteraktions.entities.requests.ApplicationCommandRequest

class HttpRequestManager(
    val m: InteractionsServer,
    val request: ApplicationCommandRequest
) : RequestManager {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    override suspend fun defer() = throw RuntimeException("Can't defer a interaction that already had a message sent!")

    override suspend fun sendMessage(message: MessageToBeSent): Message {
        val response = m.http.post<HttpResponse>("https://discord.com/api/v8/webhooks/390927821997998081/${request.token}") {
            contentType(ContentType.Application.Json)

            body = buildJsonObject {
                put("content", message.content)
                put("tts", message.tts)
                put("flags", message.flags)
            }.toString()
        }

        println(response.status)
        println(response.readText())

        return OriginalMessage("PLEASE FIX")
    }
}
package com.company.ktorchatapp.data.remote

import com.company.ktorchatapp.data.remote.dto.MessageDto
import com.company.ktorchatapp.domain.model.Message
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import java.lang.Exception

class MessageServiceImpl(
    private val client: HttpClient
): MessageService {
    override suspend fun getAllMessages(): List<Message> {
         return try {
             val response: HttpResponse = client.get(MessageService.Endpoints.GetAllMessages.url)
             val jsonMessages: String = response.bodyAsText()
             val messages: List<MessageDto> = Json.decodeFromString(jsonMessages)
             messages.map { it.toMessage() }
        } catch (e: Exception){
            emptyList()
        }
    }
}



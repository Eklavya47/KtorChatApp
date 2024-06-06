package com.company.ktorchatapp.data.remote

import com.company.ktorchatapp.domain.model.Message

interface MessageService {
    suspend fun getAllMessages(): List<Message>

    companion object{
        const val BASE_URL = "http://192.168.100.227:8080"
    }
    sealed class Endpoints(val url: String){
        object GetAllMessages: Endpoints("$BASE_URL/messages")
    }
}
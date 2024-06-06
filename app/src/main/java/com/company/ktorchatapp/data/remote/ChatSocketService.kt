package com.company.ktorchatapp.data.remote

import com.company.ktorchatapp.domain.model.Message
import com.company.ktorchatapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface ChatSocketService {
    suspend fun initSession(
        userName: String
    ): Resource<Unit>

    suspend fun sendMessage(message: String)

    fun observeMessage(): Flow<Message>

    suspend fun closeSession()

    companion object{
        const val BASE_URL = "ws://192.168.100.227:8080"
    }
    sealed class Endpoints(val url: String){
        object ChatSocket: Endpoints("$BASE_URL/chat-socket")
    }
}
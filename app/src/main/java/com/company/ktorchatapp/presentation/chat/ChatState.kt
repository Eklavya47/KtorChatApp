package com.company.ktorchatapp.presentation.chat

import com.company.ktorchatapp.domain.model.Message

data class ChatState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false
)

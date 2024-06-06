package com.company.ktorchatapp.data.remote.dto

import com.company.ktorchatapp.domain.model.Message
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.DateFormat
import java.util.Date

@Serializable
data class MessageDto(
    val text: String,
    val timeStamp: Long,
    val userName: String,
    @SerialName("_id")
    val id: String
) {
    fun toMessage(): Message{
        val date = Date(timeStamp)
        val formattedDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(date)
        return Message(
            text,
            formattedDate,
            userName)
    }
}
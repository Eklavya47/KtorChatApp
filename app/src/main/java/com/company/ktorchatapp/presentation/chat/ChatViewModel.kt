package com.company.ktorchatapp.presentation.chat

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.ktorchatapp.data.remote.ChatSocketService
import com.company.ktorchatapp.data.remote.MessageService
import com.company.ktorchatapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageService: MessageService,
    private val chatSocketService: ChatSocketService,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _messageText = mutableStateOf("")
    val messageText: State<String> = _messageText

    private val _state = mutableStateOf(ChatState())
    val state: State<ChatState> = _state

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    fun onMessageChange(message: String){
        _messageText.value = message
    }

    fun connectToChat(){
        getAllMessages()
        savedStateHandle.get<String>("userName")?.let {
            viewModelScope.launch{
                val result = chatSocketService.initSession(it)
                when(result){
                    is Resource.Success ->{
                        chatSocketService.observeMessage().onEach {newMessage ->
                                val newList = state.value.messages.toMutableList().apply {
                                    add(newMessage)
                                }
                                _state.value = state.value.copy(messages = newList)
                            }.launchIn(viewModelScope)
                    }
                    is Resource.Error ->{
                        _toastEvent.emit(result.message ?: "Unknown error found")
                    }
                }
            }
        }
    }

    fun disconnect(){
        viewModelScope.launch {
            chatSocketService.closeSession()
        }
    }

    fun getAllMessages(){
        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true)
            val result = messageService.getAllMessages()
            _state.value = state.value.copy(
                messages = result,
                isLoading = false
            )
        }
    }

    fun sendMessage(){
        viewModelScope.launch {
            val message = messageText.value.trim()
            if (message.isNotBlank()){
                chatSocketService.sendMessage(message)
                _messageText.value = ""
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}
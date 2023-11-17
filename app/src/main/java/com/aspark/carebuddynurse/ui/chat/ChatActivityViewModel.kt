package com.aspark.carebuddynurse.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspark.carebuddynurse.chat.ChatMessage
import com.aspark.carebuddynurse.chat.MessageData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatActivityViewModel @Inject constructor(
    private val chatMessage: ChatMessage
): ViewModel() {

    @OptIn(DelicateCoroutinesApi::class)
    fun sendMessage(messageData: MessageData) {

        GlobalScope.launch(Dispatchers.IO) {
            chatMessage.sendMessage(messageData)
        }
    }

    fun getChatMessage(): ChatMessage {

        return chatMessage
    }
}
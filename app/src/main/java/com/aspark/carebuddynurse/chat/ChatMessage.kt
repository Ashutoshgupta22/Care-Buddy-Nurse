package com.aspark.carebuddynurse.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jxmpp.jid.impl.JidCreate
import javax.inject.Inject

class ChatMessage @Inject constructor(private val  connection: XMPPTCPConnection) {

    private val _receivedMessage = MutableLiveData<MessageData>()
    val receivedMessage: LiveData<MessageData> = _receivedMessage

    fun sendMessage(messageData: MessageData) {

        if (connection.isConnected) {
            Log.i("XMPP Connection", "Connection is already established")

            val chatManager = ChatManager.getInstanceFor(connection)
            val jid = JidCreate.entityBareFrom("user52@aspark-care-buddy.ap-south-1.elasticbeanstalk.com")
            val chat = chatManager.chatWith(jid)

            chat.send(messageData.content)
            Log.i("Message", "sendMessage: Message sent")
        } else {
            Log.e("XMPP Connection", "Connection not established $connection")
        }
    }

    fun receiveMessage() {
        
        val chatManager = ChatManager.getInstanceFor(connection)
        chatManager.addIncomingListener {
            from, message, chat ->

            _receivedMessage.postValue( MessageData( message.body, false, ""))

            Log.i("ChatMessage", "receiveMessage:from-$from -${message.body} ")
        }
    }
}
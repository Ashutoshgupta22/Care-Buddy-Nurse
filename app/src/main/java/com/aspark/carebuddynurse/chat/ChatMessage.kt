package com.aspark.carebuddynurse.chat

import android.util.Log
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jxmpp.jid.impl.JidCreate
import javax.inject.Inject

class ChatMessage @Inject constructor(private val  connection: XMPPTCPConnection){

    fun sendMessage(messageData: MessageData) {

        if (connection.isConnected) {
            Log.i("XMPP Connection", "Connection is already established")

            val chatManager = ChatManager.getInstanceFor(connection)
            val jid = JidCreate.entityBareFrom("user2@aspark-care-buddy.ap-south-1.elasticbeanstalk.com")
            val chat = chatManager.chatWith(jid)

            chat.send(messageData.content)
            Log.i("Message", "sendMessage: Message sent")
        } else {
            Log.e("XMPP Connection", "Connection not established $connection")
        }
    }
}
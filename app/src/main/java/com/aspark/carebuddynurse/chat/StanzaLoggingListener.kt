package com.aspark.carebuddynurse.chat

import android.util.Log
import org.jivesoftware.smack.StanzaListener
import org.jivesoftware.smack.packet.Stanza

class StanzaLoggingListener: StanzaListener {

    override fun processStanza(packet: Stanza?) {

        Log.i("StanzaLoggingListener", "processStanza: Sent packet: ${packet?.toXML()}")
//        if (packet.from.equals(connection!!.user)) {
//        }
    }
}
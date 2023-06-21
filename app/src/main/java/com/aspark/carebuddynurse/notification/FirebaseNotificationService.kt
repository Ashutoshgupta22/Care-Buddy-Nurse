package com.aspark.carebuddynurse.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseNotificationService : FirebaseMessagingService(){

    override fun onNewToken(token: String) {

        Log.i("FirebaseNotificationService", "onNewToken: RefreshedToken: $token")

        val preferences = getSharedPreferences(packageName, MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("firebase_token", token)
        editor.apply()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.i("FirebaseNotificationService", "onMessageReceived: " +
                "notification received from firebase")

        val title = message.notification?.title
        val description = message.notification?.body
    }
}
package com.aspark.carebuddynurse.chat

data class MessageData(
    val content: String,
    val sentByMe: Boolean,
    val timeStamp: String
)

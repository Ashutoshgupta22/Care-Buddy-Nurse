package com.aspark.carebuddynurse.ui.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aspark.carebuddynurse.chat.MessageData
import com.aspark.carebuddynurse.model.Nurse
import com.aspark.carebuddynurse.ui.chat.ui.theme.CareBuddyNurseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : ComponentActivity() {

    private val viewModel: ChatActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var list = arrayListOf("Hi", "Hello", "how are you?", "i m fine",
            "How are you doing ", "Thanks for asking",
            "Hi", "Hello", "how are you?", "i m fine",
            "How are you doing ", "Thanks for asking",
            "Hi", "Hello", "how are you?", "i m fine",
            "How are you doing ", "Thanks for asking",
            "Hi", "Hello", "how are you? This is a big message " +
                    "just for the sake of testing . Do not panic." +
                    "Keep calm and find the nearest exit", "i m fine",
            "How are you doing ", "Thanks for asking",
            "Hi", "Hello", "how are you?", "i m fine",
            "How are you doing ", "Thanks for asking",
            "DO NOT TEXT ME")

        val messagesList = arrayListOf<MessageData>()

        for ((count,i) in list.withIndex()) {
            val c = count %2==0
            messagesList.add(MessageData(i, c,""))
        }

//        val nurse = Nurse()
//        nurse.firstName = "John"
//        nurse.lastName = "Doe"

        setContent {
            CareBuddyNurseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatScreen(nurse = Nurse.currentNurse, messageData = messagesList,{
                        finish()
                    }) {
                        viewModel.sendMessage(it)
                    }
                }
            }
        }
    }
}

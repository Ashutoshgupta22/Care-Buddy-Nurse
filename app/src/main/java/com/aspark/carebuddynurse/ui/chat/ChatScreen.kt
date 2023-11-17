package com.aspark.carebuddynurse.ui.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import com.aspark.carebuddynurse.R
import com.aspark.carebuddynurse.chat.ChatMessage
import com.aspark.carebuddynurse.chat.MessageData
import com.aspark.carebuddynurse.model.Nurse
import com.aspark.carebuddynurse.ui.chat.ui.theme.CareBuddyNurseTheme
import kotlinx.coroutines.launch

/** Using List instead of ArrayList, because list is immutable
arrayList is mutable which causes problem with recomposition **/

@Composable
fun ChatScreen(
    nurse: Nurse, messageData: List<MessageData>,
    chatMessage: ChatMessage, lifecycleOwner: LifecycleOwner,
    onUpBtnClick: () -> Unit,
    onSendClick: (MessageData) -> Unit) {

    var messageList by rememberSaveable { mutableStateOf(messageData) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        ChatActionBar(nurse, onUpBtnClick)
        Messages( Modifier.weight(1f), messageList, listState)

        LaunchedEffect(key1 = null) {
            // scroll to bottom when screen is visible
            coroutineScope.launch {
                listState.scrollToItem(messageList.lastIndex)
            }
        }

        InputCard {
            onSendClick(it)
            messageList = ArrayList(messageList + it)
            //messageList.add(it)  // this does not trigger recomposition

            coroutineScope.launch {
                listState.animateScrollToItem(messageList.lastIndex)

            }
        }
    }

    DisposableEffect(Unit) {
        chatMessage.receiveMessage() // Call receiveMessage to start listening for incoming messages
        onDispose { /* Any cleanup code if needed */ }
    }

    LaunchedEffect(chatMessage.receivedMessage) {
        // Observe changes in receivedMessage
        chatMessage.receivedMessage.observe(lifecycleOwner) { newMessage ->
            newMessage?.let {
                messageList = ArrayList(messageList + it)

                coroutineScope.launch {
                    listState.animateScrollToItem(messageList.lastIndex)
                }
            }
        }
    }
}

@Composable
fun ChatActionBar(nurse: Nurse, onUpBtnClick: () -> Unit) {

    Surface(shape = RectangleShape,
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {

            IconButton(onClick = { onUpBtnClick() },
                modifier = Modifier
                    .wrapContentSize()) {

                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Up")
            }


            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Profile pic",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "${nurse.firstName} ${nurse.lastName}",
                fontSize = 20.sp

            )
        }
    }

//    Scaffold(topBar = {
//
//        TopAppBar(title = { /*TODO*/ })
//    }) {
//
//    }
}

@Composable
private fun Messages(
    modifier: Modifier, messageDataList: List<MessageData>,
    listState: LazyListState
) {

    LazyColumn(state = listState,
        modifier = modifier
            .fillMaxSize()
    ) {
        items(messageDataList) { message ->

            if (message.sentByMe){

                Row(horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .padding(start = 32.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    MyMessageCard(messageData = message)

                }
            }
            else {

                Row(horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .padding(end = 32.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    OtherMessageCard(message)

                }
            }
        }
    }
}


@Composable
fun OtherMessageCard(messageData: MessageData) {

    Surface(color  = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 18.dp,
            bottomStart = 18.dp, bottomEnd = 18.dp),
        modifier = Modifier
            .padding(8.dp)) {

        Text(text = messageData.content,
            fontSize = 15.sp,
            style = TextStyle(color = Color.Black),
            modifier = Modifier
                .padding(12.dp))
    }

}

@Composable
fun MyMessageCard(messageData: MessageData) {

    Surface(color  = MaterialTheme.colorScheme.tertiaryContainer,
        shape = RoundedCornerShape(topStart = 18.dp, topEnd = 4.dp,
            bottomStart = 18.dp, bottomEnd = 18.dp),
        modifier = Modifier
            .padding(8.dp)) {

        Text(text = messageData.content,
            fontSize = 15.sp,
            style = TextStyle(color = Color.Black),
            modifier = Modifier
                .padding(12.dp))
    }

}

@Composable
fun InputCard(onSendClick: (MessageData) -> Unit) {

    var message by rememberSaveable { mutableStateOf("") }

    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(bottom = 6.dp, top = 6.dp)

    ) {

        TextField(
            value = message,
            onValueChange = { message = it },
            shape = RoundedCornerShape(22.dp),
            modifier = Modifier
                .weight(5f)
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(start = 6.dp),
            placeholder = { Text(text = "Message") },
            singleLine = false,
            textStyle = TextStyle(fontSize = 18.sp),
        )

        IconButton(onClick = {

            if (message.isNotBlank()) {
                onSendClick( MessageData(message.trim(),true,"") )
                message = ""
            }

        },
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .padding(start = 8.dp, end = 6.dp)
                .background(
                    MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(18.dp)
                )) {
            Icon(imageVector = Icons.Default.Send,
                tint = Color.White,
                contentDescription = "Send")

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChatScreen() {
//    val nurse =
//    nurse.firstName = "Test"
//    nurse.lastName = "Man"


    val list = arrayListOf("Hi", "Hello", "how are you?", "i m fine",
        "How are you doing ", "Thanks for asking",
        "Hi", "Hello", "how are you?", "i m fine",
        "How are you doing ", "Thanks for asking",
        "Hi", "Hello", "how are you?", "i m fine",
        "How are you doing ", "Thanks for asking",
        "Hi", "Hello", "how are you?", "i m fine",
        "How are you doing ", "Thanks for asking",
        "Hi", "Hello", "how are you?", "i m fine",
        "How are you doing ", "Thanks for asking",
        "DO NOT TEXT ME")

    val messagesList = arrayListOf<MessageData>()

    for ((count,i) in list.withIndex()) {

        val c = count %2==0
        messagesList.add(MessageData(i, c,""))

    }

//    CareBuddyNurseTheme {
//        ChatScreen(
//            nurse = Nurse.currentNurse, messageData = messagesList,
//            onUpBtnClick = {}) {}
//    }
}
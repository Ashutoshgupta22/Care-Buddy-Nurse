package com.aspark.carebuddynurse.ui.chat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.aspark.carebuddynurse.R

class ChatListFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {

            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ChatListLayout()
            }
        }
    }

    data class ChatListItem(val image: String,
                            val name: String,
                            val lastMessage: String,
                            val messageTime: String
    )

    private fun onChatItemClick() {

        val intent = Intent(requireContext(), ChatActivity::class.java)
        startActivity(intent)
    }

    @Composable
    fun ChatListLayout() {

        val item = ChatListItem("", "Test Man",
            "How you Doing?","Yesterday")

        val item2 = ChatListItem("", "John Wick",
            "Welcome to the continental!","Today")


        var chatList = arrayListOf(item, item2)

        Column {
            Text(text = "Chat", fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier
                    .padding(16.dp)
            )

            LazyColumn(modifier = Modifier
                .fillMaxSize()) {
                items(chatList) {
                    ListItem(it)
                }
            }
        }
    }

    @Composable
    fun ListItem(it: ChatListItem) {

        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable { onChatItemClick() }
        ) {

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)) {

                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "image",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(50.dp)

                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(verticalArrangement = Arrangement.Center) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = it.name,
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier
                                .wrapContentHeight()
                                .weight(1f),

                            )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = it.messageTime,
                            fontSize = 12.sp,
                            color = Color.DarkGray,
                            modifier = Modifier
                                .wrapContentSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it.lastMessage,
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        modifier = Modifier
                            .wrapContentSize()

                    )

                }
            }
        }

//        Divider(
//            modifier = Modifier.height(1.dp)
//        )
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewChatListLayout() {

        ChatListLayout()
    }
}
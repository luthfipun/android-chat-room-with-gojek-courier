package github.luthfipun.chatroom.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import github.luthfipun.chatroom.R
import github.luthfipun.chatroom.screen.ui.theme.Green200
import github.luthfipun.chatroom.screen.ui.theme.Green500

@Preview(showBackground = true)
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.fillMaxSize(),
    ){

        Column(modifier = Modifier
            .fillMaxWidth()) {
            ChatHeader()
            ChatContent(
                modifier = Modifier.weight(1f)
            )
            ChatInput()
        }

    }
}

@Composable
fun ChatHeader() {
    TopAppBar(
        title = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Chat Room",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "someone typing...",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.LightGray
                )
            }
        },
        modifier = Modifier.height(65.dp),
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = "back_icon",
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more),
                    contentDescription = "more_icon"
                )
            }
        },
        backgroundColor = Color.White,
        elevation = 8.dp
    )
}

@Composable
fun ChatInput() {

    var messageInput by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = messageInput,
        onValueChange = { messageInput = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus(true)
        }),
        singleLine = false,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Green500,
            unfocusedBorderColor = Color.LightGray
        ),
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        ),
        placeholder = {
            Text(
                text = "type anything...",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.LightGray
            )
        },
        trailingIcon = {
            IconButton(
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = "icon_send",
                    tint = Green200,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    )
}

@Composable
fun ChatBody(isMe: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 8.dp,
                bottom = 8.dp,
                start = 8.dp,
                end = 8.dp
            ),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        if (!isMe){
            ChatAvatar()
            ChatMessage(isMe = isMe)
        }else {
            ChatMessage(isMe = isMe)
            ChatAvatar()
        }
    }
}

@Composable
fun ChatContent(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        state = rememberLazyListState(),
        reverseLayout = true,
        userScrollEnabled = true
    ) {
        items(10){
            ChatBody(it % 2 == 0)
        }
    }
}

@Composable
fun ChatAvatar() {
    Image(
        painter = painterResource(id = R.drawable.person1),
        contentDescription = null,
        modifier = Modifier
            .size(40.dp)
            .border(
                border = BorderStroke(2.dp, Color.LightGray),
                shape = CircleShape
            )
            .padding(2.dp)
            .clip(CircleShape)
            .background(Color.LightGray.copy(0.5f))
    )
}

@Composable
fun ChatMessage(isMe: Boolean) {
    Column(modifier = Modifier.padding(
        top = 3.dp,
        start = if (isMe) 0.dp else 8.dp,
        end = if (isMe) 8.dp else 0.dp
    )) {
        Text(
            text = "Hello",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 21.sp,
            letterSpacing = 0.5.sp,
            color = Color.White,
            modifier = Modifier
                .widthIn(max = 250.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = if (isMe) 8.dp else 0.dp,
                        topEnd = if (isMe) 0.dp else 8.dp,
                        bottomEnd = 8.dp,
                        bottomStart = 8.dp
                    )
                )
                .background(if (isMe) Green500 else Color.Gray)
                .padding(16.dp)
        )
        Row(
            modifier = Modifier.align(if (isMe) Alignment.End else Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Hannah",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
            Text(
                text = " - 9:00",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
        }
    }
}
@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package github.luthfipun.chatroom.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import github.luthfipun.chatroom.R
import github.luthfipun.chatroom.domain.data.Message
import github.luthfipun.chatroom.domain.util.MessageInfoType
import github.luthfipun.chatroom.domain.util.MessageType
import github.luthfipun.chatroom.domain.util.messageBodyType
import github.luthfipun.chatroom.screen.ui.theme.Green200
import github.luthfipun.chatroom.screen.ui.theme.Green500

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    viewModel: MainViewModel
){
    var leaveDialogStatus by remember { mutableStateOf(false) }
    var messageInput by remember { mutableStateOf("") }
    val fakeMessages = remember { mutableStateListOf<Message>() }
    val fakeUser = viewModel.localUser.collectAsState()

    BackHandler {
        leaveDialogStatus = true
    }

    Box(
        modifier = modifier.fillMaxSize(),
    ){
        Column(modifier = Modifier
            .fillMaxWidth()) {
            ChatHeader(onBack = { leaveDialogStatus = true })
            ChatContent(
                modifier = Modifier.weight(1f),
                messages = messageBodyType(fakeMessages).reversed()
            )
            ChatInput(
                messageInput = messageInput,
                onMessageChange = {
                    messageInput = it
                },
                onSend = {
                    if (messageInput.isNotBlank()){
                        val message = Message(
                            id = System.currentTimeMillis(),
                            text = messageInput,
                            type = MessageType.TEXT,
                            time = "19:00",
                            user = fakeUser.value!!,
                            infoType = null,
                            isOwner = true
                        )
                        fakeMessages.add(message)
                        messageInput = ""
                    }
                }
            )
        }
        LeaveDialog(
            leaveDialogStatus = leaveDialogStatus,
            onLeaveDialogConfirm = {
                leaveDialogStatus = false
                onNavigateBack()
            },
            onLeaveDialogDismiss = {
                leaveDialogStatus = false
            }
        )
    }
}

@Composable
fun ChatHeader(
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Chat Room",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onPrimary
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
            IconButton(onClick = { onBack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = "back_icon",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more),
                    contentDescription = "more_icon",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
        backgroundColor = MaterialTheme.colors.background,
        elevation = 8.dp
    )
}

@Composable
fun ChatInput(
    messageInput: String,
    onMessageChange: (value: String) -> Unit,
    onSend: () -> Unit
) {

    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = messageInput,
        onValueChange = { onMessageChange(it) },
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
                onClick = { onSend() }
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatContent(
    modifier: Modifier = Modifier,
    messages: List<Message>
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        state = rememberLazyListState(),
        reverseLayout = true,
        userScrollEnabled = true
    ) {
        items(messages, key = { it.id }){ message ->
            when(message.type){
                MessageType.TEXT -> ChatBody(modifier = Modifier.animateItemPlacement(), message = message)
                MessageType.INFO -> ChatInfo(modifier = Modifier.animateItemPlacement(), message = message)
            }
        }
    }
}

@Composable
fun ChatBody(
    modifier: Modifier = Modifier,
    message: Message
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, bottom = if (message.isParent) 24.dp else 8.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = if (message.isOwner) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isOwner){
            ChatAvatar(message = message)
            ChatMessage(message = message)
        }else {
            ChatMessage(message = message)
            ChatAvatar(message = message)
        }
    }
}

@Composable
fun ChatInfo(
    modifier: Modifier = Modifier,
    message: Message
) {
    val statusInfo = when(message.type){
        MessageType.TEXT -> "Unknown"
        MessageType.INFO -> when (message.infoType){
            MessageInfoType.JOIN -> "joined"
            MessageInfoType.LEAVE -> "leave"
            else -> "Unknown"
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message.time,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray.copy(0.8f),
            modifier = Modifier
                .clip(
                    RoundedCornerShape(4.dp)
                )
                .background(Color.LightGray.copy(0.5f))
                .padding(horizontal = 12.dp, vertical = 1.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${message.user.name} $statusInfo the Room",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color.LightGray
        )
    }
}

@Composable
fun ChatAvatar(
    message: Message
) {
    Image(
        painter = painterResource(id = message.user.avatar),
        contentDescription = null,
        modifier = Modifier
            .alpha(if (message.isParent) 1f else 0f)
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
fun ChatMessage(
    message: Message
) {
    Column(modifier = Modifier.padding(
        start = if (message.isOwner) 0.dp else 8.dp,
        end = if (message.isOwner) 8.dp else 0.dp
    )) {
        Text(
            text = message.text ?: "",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 21.sp,
            letterSpacing = 0.5.sp,
            color = Color.White,
            modifier = Modifier
                .widthIn(max = 250.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = if (message.isOwner) 8.dp else if (message.isParent) 0.dp else 8.dp,
                        topEnd = if (message.isOwner) if (message.isParent) 0.dp else 8.dp else 8.dp,
                        bottomEnd = 8.dp,
                        bottomStart = 8.dp
                    )
                )
                .background(if (message.isOwner) Green500 else Color.Gray)
                .padding(16.dp)
                .align(if (message.isOwner) Alignment.End else Alignment.Start)
        )

        if (message.isParent){
            Row(
                modifier = Modifier.align(if (message.isOwner) Alignment.End else Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = message.user.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
                Text(
                    text = " - ${message.time}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun LeaveDialog(
    leaveDialogStatus: Boolean,
    onLeaveDialogDismiss: () -> Unit,
    onLeaveDialogConfirm: () -> Unit
) {
    if (leaveDialogStatus){
        AlertDialog(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            onDismissRequest = { onLeaveDialogDismiss() },
            shape = RoundedCornerShape(8.dp),
            backgroundColor = MaterialTheme.colors.background,
            title = {
                Text(
                    text = "Leave Room",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onPrimary
                )
            },
            text = {
                Text(
                    text = "Are you sure to leave the chat room?",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = { onLeaveDialogDismiss() },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.LightGray
                        )
                    ) {
                        Text(
                            text = "Cancel",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    }
                    Button(
                        onClick = { onLeaveDialogConfirm() },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Green200
                        )
                    ) {
                        Text(
                            text = "Yes, Leave Room",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    }
                }
            }
        )
    }
}
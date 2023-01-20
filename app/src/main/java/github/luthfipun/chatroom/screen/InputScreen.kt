package github.luthfipun.chatroom.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
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
fun InputScreen(
    modifier: Modifier = Modifier,
    onNavigateToChat: () -> Unit = {}
) {
    var avatarSelected by remember { mutableStateOf(-1) }
    var nameField by remember { mutableStateOf("") }
    var isReady by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = avatarSelected, key2 = nameField){
        isReady = avatarSelected > 0 && nameField.isNotBlank()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        InputHeader()
        InputAvatar(
            avatarSelected = avatarSelected,
            onAvatarSelected = {
                avatarSelected = it
            }
        )
        InputName(
            nameField = nameField,
            onNameChange = {
                nameField = it
            }
        )
        InputSubmit(
            onNavigateToChat = onNavigateToChat,
            isReady = isReady
        )
    }
}

@Composable
fun InputSubmit(
    onNavigateToChat: () -> Unit,
    isReady: Boolean
) {

    val animateColor by animateColorAsState(
        targetValue = if (isReady) Green200 else Color.LightGray,
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = 200
        )
    )

    val animateArrow by animateDpAsState(
        targetValue = if (isReady) 0.dp else 2.dp,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 500,
                delayMillis = 200
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    Button(
        onClick = { onNavigateToChat() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 24.dp),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(
            horizontal = 8.dp,
            vertical = 16.dp
        ),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = animateColor,
            disabledBackgroundColor = Color.LightGray
        ),
        enabled = isReady
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Enter Room",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )

            Image(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "button_enter_arrow",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier.absoluteOffset(x = animateArrow)
            )
        }
    }
}

@Composable
fun InputName(
    nameField: String,
    onNameChange: (value: String) -> Unit
) {

    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = nameField,
        onValueChange = { onNameChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 24.dp),
        singleLine = true,
        placeholder = {
            Text(
                text = "Your Name..",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.LightGray
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus(true)
        }),
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Green500,
            unfocusedIndicatorColor = Color.LightGray,
            backgroundColor = Color.Transparent,
            textColor = Green500
        ),
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    )
}

@Composable
fun InputAvatar(
    avatarSelected: Int,
    onAvatarSelected: (index: Int) -> Unit
) {

    val avatars = remember {
        mutableListOf(
            R.drawable.person1,
            R.drawable.person2,
            R.drawable.person3,
            R.drawable.person4,
            R.drawable.person5,
            R.drawable.person6,
            R.drawable.person7,
            R.drawable.person8
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(24.dp),
        state = rememberLazyGridState()
    ){
        items(avatars.size){
            InputAvatarItem(
                selected = avatarSelected == it,
                avatar = avatars[it],
                avatarIndex = it,
                onAvatarSelected = { currentIndex ->
                    onAvatarSelected(currentIndex)
                })
        }
    }
}

@Composable
fun InputAvatarItem(
    selected: Boolean,
    avatar: Int,
    avatarIndex: Int,
    onAvatarSelected: (index: Int) -> Unit
) {

    val animateColor by animateColorAsState(
        targetValue = if (selected) Green500 else Color.LightGray.copy(0.3f),
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = 100,
            easing = LinearOutSlowInEasing
        )
    )
    
    val animateSelected by animateFloatAsState(
        targetValue = if (selected) 1f else 0.9f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    
    IconButton(
        onClick = { onAvatarSelected(avatarIndex) },
        modifier = Modifier
            .padding(vertical = 8.dp)
            .scale(animateSelected)
    ) {
        Image(
            painter = painterResource(id = avatar),
            contentDescription = "person${avatarIndex}",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(72.dp)
                .border(
                    border = BorderStroke(
                        width = 3.dp,
                        color = animateColor
                    ),
                    shape = CircleShape
                )
                .padding(3.dp)
                .clip(CircleShape)
                .background(color = Color.White)
        )
    }
}

@Composable
fun InputHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                painter = painterResource(id = R.drawable.ic_arrow_left),
                contentDescription = "icon_back",
                tint = Green500
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Pick Avatar and Your Name",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colors.onPrimary
        )
    }
}
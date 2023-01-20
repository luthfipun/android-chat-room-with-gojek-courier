package github.luthfipun.chatroom.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import github.luthfipun.chatroom.R
import github.luthfipun.chatroom.screen.ui.theme.Green200
import github.luthfipun.chatroom.screen.ui.theme.Green500

@Preview(showBackground = true)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToInput: () -> Unit = {}
){
    var animate by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit){
        animate = true
    }

    val animateArrow by animateDpAsState(
        targetValue = if (animate) 0.dp else 2.dp,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 500,
                delayMillis = 200
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_chat_logo),
            contentDescription = "logo",
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Green500
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { onNavigateToInput() },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 24.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Green200.copy(0.1F)),
            border = BorderStroke(1.5.dp, Green200),
            elevation = null
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        painter = painterResource(id = R.drawable.ic_chat),
                        contentDescription = "button_chat_icon",
                        tint = Green200
                    )

                    Spacer(modifier = modifier.width(12.dp))

                    Text(
                        text = "Join Room",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onPrimary
                    )
                }

                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .absoluteOffset(x = animateArrow),
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = "button_chat_icon_arrow",
                    tint = Green200
                )
            }
        }
    }
}
package github.luthfipun.chatroom.screen

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        InputHeader()
        InputAvatar()
        InputName()
        InputSubmit()
    }
}

@Composable
fun InputSubmit() {
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 24.dp),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(
            horizontal = 8.dp,
            vertical = 16.dp
        ),
        colors = ButtonDefaults.buttonColors(backgroundColor = Green200)
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
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    }
}

@Composable
fun InputName() {

    var nameFields by remember { mutableStateOf("") }

    OutlinedTextField(
        value = nameFields,
        onValueChange = { nameFields = it },
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
fun InputAvatar() {

    val avatars = listOf<Int>(
        R.drawable.person1,
        R.drawable.person2,
        R.drawable.person3,
        R.drawable.person4,
        R.drawable.person5,
        R.drawable.person6,
        R.drawable.person7,
        R.drawable.person8
    )

    var avatarSelected by remember { mutableStateOf(-1) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(24.dp),
        state = rememberLazyGridState()
    ){
        items(avatars.size){
            IconButton(
                onClick = { avatarSelected = it },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = avatars[it]),
                    contentDescription = "person${it}",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(72.dp)
                        .border(
                            border = BorderStroke(
                                width = 3.dp,
                                color = if (avatarSelected == it) Green500 else Color.LightGray.copy(0.3f)
                            ),
                            shape = CircleShape
                        )
                        .padding(3.dp)
                        .clip(CircleShape)
                        .background(color = Color.White)
                )
            }
        }
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
            fontWeight = FontWeight.SemiBold
        )
    }
}
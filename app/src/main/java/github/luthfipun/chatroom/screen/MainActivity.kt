package github.luthfipun.chatroom.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import github.luthfipun.chatroom.screen.ui.theme.ChatRoomTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatRoomTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavHost(navController = navController, startDestination = "home"){
                        composable("home"){
                            HomeScreen(onNavigateToInput = {
                                navController.navigate("input")
                            })
                        }

                        composable("input"){
                            InputScreen(onNavigateToChat = {
                                navController.navigate("chat")
                            })
                        }

                        composable("chat"){
                            ChatScreen()
                        }
                    }
                }
            }
        }
    }
}
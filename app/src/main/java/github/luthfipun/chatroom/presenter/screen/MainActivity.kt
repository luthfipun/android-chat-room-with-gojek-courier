package github.luthfipun.chatroom.presenter.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import github.luthfipun.chatroom.presenter.screen.ui.theme.ChatRoomTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatRoomTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                val mainViewModel: MainViewModel = viewModel()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavHost(navController = navController, startDestination = "home"){
                        composable("home"){
                            HomeScreen(
                                onNavigateToInput = {
                                    navController.navigate("input")
                                },
                                viewModel = mainViewModel
                            )
                        }

                        composable("input"){
                            InputScreen(
                                onNavigateToChat = {
                                    navController.navigate("chat"){
                                        popUpTo("home")
                                    }
                                },
                                onNavigateBack = {
                                    navController.popBackStack()
                                },
                                viewModel = mainViewModel
                            )
                        }

                        composable("chat"){
                            ChatScreen(
                                onNavigateBack = {
                                    navController.popBackStack()
                                },
                                viewModel = mainViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
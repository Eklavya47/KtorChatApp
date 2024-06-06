package com.company.ktorchatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.company.ktorchatapp.presentation.chat.ChatScreen
import com.company.ktorchatapp.presentation.username.UserNameScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "userName_screen"
            ){
                composable("userName_screen"){
                    UserNameScreen(onNavigate = navController::navigate)
                }
                composable(
                    "chat_screen/{userName}",
                    arguments = listOf(
                        navArgument(name = "userName"){
                            type = NavType.StringType
                            nullable = true
                        }
                    )
                ){
                    val userName = it.arguments?.getString("userName")
                    ChatScreen(userName = userName)
                }
            }
        }
    }
}

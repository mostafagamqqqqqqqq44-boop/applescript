package com.applescript.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.applescript.app.ui.theme.AppleScriptTheme
import com.applescript.app.ui.screens.LoginScreen
import com.applescript.app.ui.screens.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppleScriptTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppleScriptApp()
                }
            }
        }
    }
}

@Composable
fun AppleScriptApp() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { accountId ->
                    navController.navigate("home/$accountId")
                }
            )
        }
        composable("home/{accountId}") { backStackEntry ->
            val accountId = backStackEntry.arguments?.getString("accountId") ?: ""
            HomeScreen(
                accountId = accountId,
                onLogout = {
                    navController.popBackStack()
                }
            )
        }
    }
}

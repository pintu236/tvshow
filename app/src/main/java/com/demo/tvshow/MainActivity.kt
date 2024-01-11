package com.demo.tvshow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.demo.tvshow.ui.home.Home
import com.demo.tvshow.ui.showdetails.ShowDetails
import com.demo.tvshow.ui.theme.TVShowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TVShowTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TVShowApp()
                }
            }
        }
    }


}

@Composable
private fun TVShowApp(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { Home(navController) }
        composable(
            "${Screen.ShowDetails.route}?serial_id={serial_id}",
            arguments = listOf(
                navArgument("serial_id") {
                    type = NavType.IntType
                    defaultValue = 0
                },
            )
        ) { backStackEntry ->
            ShowDetails(backStackEntry.arguments?.getInt("serial_id") ?: 0)
        }
        // Add more destinations similarly.
    }
}


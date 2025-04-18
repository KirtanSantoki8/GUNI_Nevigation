package com.devkt.guninevigation.screens.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.devkt.guninevigation.screens.AllPlacesScreen
import com.devkt.guninevigation.screens.HomeScreen
import com.devkt.guninevigation.screens.LoginScreen
import com.devkt.guninevigation.screens.OtherPlacesScreen
import com.devkt.guninevigation.screens.RegisterScreen
import com.devkt.guninevigation.screens.SubPlacesScreen

@Composable
fun App(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routs.LoginScreen) {
        composable<Routs.LoginScreen> {
            LoginScreen(navController = navController)
        }

        composable<Routs.RegisterScreen> {
            RegisterScreen(navController = navController)
        }

        composable<Routs.HomeScreen> {
            HomeScreen(navController = navController)
        }

        composable<Routs.AllPlacesScreen> {
            AllPlacesScreen(navController = navController)
        }

        composable<Routs.OtherPlacesScreen> {
            OtherPlacesScreen(navController = navController)
        }

        composable<Routs.SubPlacesScreen> {
            val data = it.toRoute<Routs.SubPlacesScreen>()
            SubPlacesScreen(navController = navController, mainLocation = data.mainLocation)
        }
    }
}
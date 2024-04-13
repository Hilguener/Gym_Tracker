package com.hilguener.gymtracker.navigation

sealed class Screens(val route: String) {
    object LoginScreen : Screens(route = "Login_Screen")

    object RegisterScreen : Screens(route = "Register_Screen")

    object HomeScreen : Screens(route = "Home_Screen")
}

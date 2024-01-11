package com.demo.tvshow

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ShowDetails : Screen("show_details")
}
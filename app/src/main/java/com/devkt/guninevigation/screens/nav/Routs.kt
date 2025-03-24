package com.devkt.guninevigation.screens.nav

import kotlinx.serialization.Serializable

sealed class Routs {
    @Serializable
    object RegisterScreen

    @Serializable
    object LoginScreen

    @Serializable
    object HomeScreen

    @Serializable
    object AllPlacesScreen
}
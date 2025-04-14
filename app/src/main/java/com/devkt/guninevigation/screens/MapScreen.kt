package com.devkt.guninevigation.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.devkt.guninevigation.screens.map.rememberMapViewWithLifecycle
import com.devkt.guninevigation.viewModel.NavigationViewModel

@Composable
fun MapScreen(
    modifier: Modifier,
    viewModel: NavigationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val mapViewLifecycle = rememberMapViewWithLifecycle()

}
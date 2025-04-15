package com.devkt.guninevigation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.devkt.guninevigation.screens.map.MapActivity
import com.devkt.guninevigation.ui.theme.GUNINevigationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GUNINevigationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val intent = Intent(this, MapActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
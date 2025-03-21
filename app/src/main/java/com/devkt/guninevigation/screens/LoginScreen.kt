package com.devkt.guninevigation.screens

import android.graphics.Paint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devkt.guninevigation.R
import com.devkt.guninevigation.viewModel.MyViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: MyViewModel = hiltViewModel()
) {
    val state = viewModel.loginUser.collectAsState()
    val context = LocalContext.current

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    when {
        state.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                CircularProgressIndicator()
            }
        }
        state.value.error != null -> {
            Text(text = state.value.error!!, modifier = Modifier.padding(50.dp))
        }
        state.value.data != null -> {
            Toast.makeText(context, "Logged In Successfully", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(top = 70.dp, start = 10.dp, end = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.guni_logo),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Login",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text(text = "Email") },
                modifier = Modifier.padding(top = 10.dp)
            )
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text(text = "Password") },
                modifier = Modifier.padding(top = 10.dp)
            )
            Button(
                onClick = {
                    if (email.value == "" || password.value == "") {
                        Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        viewModel.login(email.value, password.value)
                    }
                },
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text(text = "Login")
            }
            Row(
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text(
                    text = "Not Registered yet?"
                )
                Text(
                    text = "Register here",
                    modifier = Modifier.padding(start = 5.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF9800)
                )
            }
        }
    }
}
package com.devkt.guninevigation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.devkt.guninevigation.R
import okhttp3.internal.wait

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Scaffold(
        topBar = {
            Surface(
                shadowElevation = 10.dp
            ) {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.guni_logo),
                                contentDescription = null,
                                modifier = Modifier.height(55.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxSize()
                                    .padding(end = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(
                                    onClick = {

                                    },
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.menu),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxWidth()
                                            .height(20.dp)
                                    )
                                }
                                IconButton(
                                    onClick = {

                                    },
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.search),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxWidth()
                                            .height(25.dp)
                                    )
                                }
                            }
                        }
                    },
                    modifier = Modifier.height(120.dp),
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome to our Campus!",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 35.dp)
                )
                Text(
                    text = "Explore our university campus and discover all the\namazing places it has to offer",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 7.dp)
                )
                Button(
                    onClick = {

                    },
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(Color.Black),
                    elevation = ButtonDefaults.buttonElevation(10.dp),
                    modifier = Modifier.padding(top = 30.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Start Exploring",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        Image(
                            painter = painterResource(id = R.drawable.white_arrow),
                            contentDescription = null,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                }
                Row {
                    Card(
                        modifier = Modifier.padding(top = 40.dp, start = 30.dp, end = 30.dp, bottom = 20.dp)
                            .fillMaxSize(),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.sample1),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "Main Building",
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp,
                            modifier = Modifier.padding(top = 20.dp, start = 10.dp)
                        )
                        Text(
                            text = "The heart of our campus, where all the administrative offices are located.",
                            fontSize = 15.sp,
                            modifier = Modifier.padding(top = 10.dp, start = 10.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = {

                                },
                                shape = RoundedCornerShape(5.dp),
                                colors = ButtonDefaults.buttonColors(Color.Black),
                                modifier = Modifier.padding(top = 20.dp)
                                    .height(40.dp)
                                    .width(120.dp)
                            ) {
                                Text(
                                    text = "Visit",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
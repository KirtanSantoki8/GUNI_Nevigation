package com.devkt.guninevigation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.devkt.guninevigation.R

@Composable
fun AllPlacesScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val places = listOf(
        "Hostels",
        "Colleges",
        "Mess",
        "Sports Complex",
        "Gym",
        "Hospital",
        "Parking",
        "Canteen",
        "Others"
    )
    val placesImages = listOf(
        R.drawable.hostel,
        R.drawable.colleges,
        R.drawable.mess,
        R.drawable.sport_ball,
        R.drawable.gym,
        R.drawable.hospital,
        R.drawable.parking,
        R.drawable.canteen,
        R.drawable.other_2_svgrepo_com
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 70.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Choose a place to visit",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
            )
            Image(
                painter = painterResource(id = R.drawable.close_sm_svgrepo_com),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 25.dp)
                    .height(45.dp)
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(top = 50.dp, start = 10.dp, end = 10.dp)
        ) {
            items(places.size) {
                Card(
                    modifier = Modifier.height(130.dp)
                        .width(60.dp)
                        .clickable(
                            onClick = {

                            }
                        )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = placesImages[it]),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.height(90.dp)
                                .width(90.dp)
                                .fillMaxWidth()
                                .padding(top = 20.dp)
                        )
                        Text(
                            text = places[it],
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                }
            }
        }
    }
}
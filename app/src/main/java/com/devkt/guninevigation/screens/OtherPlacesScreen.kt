package com.devkt.guninevigation.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.devkt.guninevigation.R
import com.devkt.guninevigation.screens.nav.Routs
import com.devkt.guninevigation.viewModel.GetMoreLocationsViewModel

@Composable
fun OtherPlacesScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: GetMoreLocationsViewModel = hiltViewModel()
) {
    val state = viewModel.getMoreLocations.collectAsState()

    LaunchedEffect(
        key1 = true
    ) {
        viewModel.getMoreLocations()
    }

    when {
        state.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.value.error != null -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "No Data Found.")
            }
        }

        state.value.data != null -> {
            val data = state.value.data!!.message
            val status = state.value.data!!.status
            if (status == 400) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "No Data Found.")
                }
            } else {
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
                                .clickable(
                                    onClick = {
                                        navController.popBackStack()
                                    }
                                )
                        )
                    }
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(top = 50.dp, start = 10.dp, end = 10.dp)
                    ) {
                        items(data.size) {
                            Card(
                                modifier = Modifier
                                    .height(130.dp)
                                    .width(90.dp)
                                    .clickable(
                                        onClick = {
                                            navController.navigate(Routs.SubPlacesScreen(data[it][3].toString()))
                                        }
                                    )
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    AsyncImage(
                                        model = data[it][2],
                                        contentDescription = null,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier
                                            .height(90.dp)
                                            .width(90.dp)
                                            .fillMaxWidth()
                                            .padding(top = 20.dp)
                                    )
                                    Text(
                                        text = data[it][3].toString(),
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        modifier = Modifier.padding(top = 10.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
package com.devkt.guninevigation.screens

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.devkt.guninevigation.R
import com.devkt.guninevigation.screens.map.MapActivity
import com.devkt.guninevigation.viewModel.GetMoreLocationsViewModel

@Composable
fun SubPlacesScreen(
    modifier: Modifier = Modifier,
    viewModel: GetMoreLocationsViewModel = hiltViewModel(),
    navController: NavController,
    mainLocation: String
) {
    val state = viewModel.getSubLocations.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(
        key1 = true
    ) {
        viewModel.getSubLocations(mainLocation)
    }

    when {
        state.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.value.error != null -> {
            Text(text = state.value.error!!, modifier = Modifier.padding(50.dp))
        }

        state.value.data != null -> {
            val data = state.value.data!!.message
            val status = state.value.data!!.status
            if (status == 400) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
                    LazyColumn(
                        modifier = Modifier.padding(top = 50.dp)
                    ) {
                        items(data.size) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                                    .padding(top = 2.dp)
                                    .clickable {
                                        val intent = Intent(context, MapActivity::class.java)
                                        intent.putExtra("longitude", (data[it][7] as? String)?.toDoubleOrNull())
                                        intent.putExtra("latitude", (data[it][8] as? String)?.toDoubleOrNull())
                                        context.startActivity(intent)
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = data[it][3],
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .width(70.dp)
                                            .padding(top = 5.dp, bottom = 5.dp, start = 10.dp)
                                    )
                                    Text(
                                        text = data[it][4].toString(),
                                        fontSize = 18.sp,
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.white_arrow),
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 10.dp)
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
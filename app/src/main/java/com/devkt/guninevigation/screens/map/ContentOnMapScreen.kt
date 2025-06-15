package com.devkt.guninevigation.screens.map

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.devkt.guninevigation.R
import com.devkt.guninevigation.viewModel.GetSpecificSubLocationViewModel
import com.mapbox.maps.MapView
import com.mapbox.navigation.ui.maps.camera.NavigationCamera
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class NavigationUIState {
    SHOW_DESTINATION,
    SHOW_ROUTE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentOnMapScreen(
    onMapViewReady: (MapView) -> Unit,
    onStartNavigation: () -> Unit,
    onStopNavigation: () -> Unit,
    viewModel: GetSpecificSubLocationViewModel = hiltViewModel(),
    subLocationName: String
) {

    var uiState by remember { mutableStateOf(NavigationUIState.SHOW_DESTINATION) }

    val state = viewModel.getSpecificSubLocation.collectAsState()

    var name = ""
    var description = ""
    var phoneNo = ""
    var imageUrl = ""

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = Unit) {
        viewModel.getSpecificSubLocation(subLocationName)
    }

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            skipPartiallyExpanded = false,
            initialValue = SheetValue.Expanded,
            density = LocalDensity.current,
            confirmValueChange = { true },
            skipHiddenState = true
        )
    )

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 90.dp,
        sheetShadowElevation = 8.dp,
        sheetTonalElevation = 1.dp,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .verticalScroll(scrollState),
            ) {
                when {
                    state.value.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    state.value.error != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "No Data Found.")
                        }
                    }

                    state.value.data != null -> {
                        state.value.data!!.message.firstOrNull()?.let {
                            name = it.name
                            description = it.description
                            phoneNo = it.phone_no
                            imageUrl = it.imageUrl
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 2
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                            Card(
                                modifier = Modifier
                                    .height(180.dp)
                                    .padding(start = 40.dp, end = 40.dp)
                            ) {
                                AsyncImage(
                                    model = imageUrl,
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "\" $description \"",
                            fontSize = 17.sp,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier.padding(start = 25.dp, end = 25.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.padding(start = 25.dp, end = 25.dp, bottom = 15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.call),
                                contentDescription = null,
                                modifier = Modifier.size(17.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "Call on")
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = phoneNo,
                                color = Color(0xFF2196F3),
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier.clickable(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL).apply {
                                            data = "tel:$phoneNo".toUri()
                                        }
                                        context.startActivity(intent)
                                    }
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        val context = LocalContext.current
        AndroidView(
            factory = {
                val view = MapView(context)
                onMapViewReady(view)
                view
            },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            if (uiState == NavigationUIState.SHOW_ROUTE) {
                IconButton(
                    onClick = {
                        uiState = NavigationUIState.SHOW_DESTINATION
                        onStopNavigation()
                        scope.launch {
                            scaffoldState.bottomSheetState.expand()
                        }
                    },
                    modifier = Modifier
                        .padding(top = 60.dp, end = 10.dp)
                        .align(Alignment.TopEnd)
                        .border(
                            border = BorderStroke(2.dp, Color.Black),
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.close_sm_svgrepo_com),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            val padding = if (scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded) 400.dp else 90.dp

            Button(
                onClick = {
                    if (uiState == NavigationUIState.SHOW_ROUTE) {

                    }
                    else {
                        uiState = NavigationUIState.SHOW_ROUTE
                        onStartNavigation()
                        scope.launch {
                            scaffoldState.bottomSheetState.partialExpand()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(Color.Black),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 8.dp, bottom = padding)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = if (uiState == NavigationUIState.SHOW_ROUTE) {
                            painterResource(R.drawable.navigation_svgrepo_com__1_)
                        }
                        else {
                            painterResource(R.drawable.navigation_svgrepo_com)
                        },
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(25.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (uiState == NavigationUIState.SHOW_ROUTE) {
                            "Recenter"
                        }
                        else {
                            "Start"
                        },
                        color = Color.White
                    )
                }
            }
        }
    }
}
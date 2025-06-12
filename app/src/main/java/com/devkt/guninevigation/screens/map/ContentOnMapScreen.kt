package com.devkt.guninevigation.screens.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.devkt.guninevigation.viewModel.GetSpecificSubLocationViewModel
import com.mapbox.maps.MapView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentOnMapScreen(
    onMapViewReady: (MapView) -> Unit,
    viewModel: GetSpecificSubLocationViewModel = hiltViewModel(),
    subLocationName: String
) {

    val state = viewModel.getSpecificSubLocation.collectAsState()

    var name = ""
    var description = ""
    var phoneNo = ""
    var imageUrl = ""

    LaunchedEffect(key1 = Unit) {
        viewModel.getSpecificSubLocation(subLocationName)
    }

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            skipPartiallyExpanded = false,
            initialValue = SheetValue.PartiallyExpanded,
            density = LocalDensity.current,
            confirmValueChange = { true },
            skipHiddenState = true
        )
    )

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 100.dp,
        sheetShadowElevation = 8.dp,
        sheetTonalElevation = 1.dp,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when {
                    state.value.isLoading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    state.value.error != null -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
                        Text(
                            text = name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Row {
                            Text(text = imageUrl)
                            Column {
                                Text(text = description)
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(text = phoneNo)
                            }
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
    }
}
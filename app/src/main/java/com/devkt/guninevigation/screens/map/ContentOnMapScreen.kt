package com.devkt.guninevigation.screens.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.mapbox.maps.MapView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentOnMapScreen(onMapViewReady: (MapView) -> Unit) {
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
                    .padding(16.dp)
            ) {
                Text("This is the bottom sheet content.")
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

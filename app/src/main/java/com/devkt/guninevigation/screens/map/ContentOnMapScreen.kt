package com.devkt.guninevigation.screens.map

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentOnMapScreen(modifier: Modifier = Modifier) {

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
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetPeekHeight = 100.dp,
        sheetShadowElevation = 50.dp,
        sheetTonalElevation = 50.dp,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .padding(10.dp)
            ) {
                Text("This is the bottom sheet")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Main Content")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Test() {
    ContentOnMapScreen()
}
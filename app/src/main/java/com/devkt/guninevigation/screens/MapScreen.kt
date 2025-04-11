package com.devkt.guninevigation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.devkt.guninevigation.R
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyle
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location

@Composable
fun MapScreen(modifier: Modifier = Modifier) {
    val mapViewportState = rememberMapViewportState()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        MapboxMap(
            Modifier.fillMaxSize()
                .padding(top = 50.dp),
            mapViewportState = mapViewportState,
            style = { MapStyle(style = Style.MAPBOX_STREETS) }
        ) {
            MapEffect(Unit) { mapView ->
                mapView.location.updateSettings {
                    locationPuck = createDefault2DPuck(withBearing = true)
                    enabled = true
                    puckBearing = PuckBearing.COURSE
                    puckBearingEnabled = true
                    pulsingEnabled = true
                }
            }
            mapViewportState.transitionToFollowPuckState()
            val marker = rememberIconImage(key = R.drawable.red_marker, painter = painterResource(R.drawable.red_marker))
            PointAnnotation(point = Point.fromLngLat(72.458056,23.530075)) {
                iconImage = marker
            }
//            CircleAnnotation(point = Point.fromLngLat(72.458056,23.530075)) {
//                circleRadius = 8.0
//                circleColor = Color(0xffee4e8b)
//                circleStrokeWidth = 2.0
//                circleStrokeColor = Color(0xffffffff)
//            }
        }
    }
}
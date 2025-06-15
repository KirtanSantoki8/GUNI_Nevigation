package com.devkt.guninevigation.screens.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.devkt.guninevigation.R
import com.devkt.guninevigation.ui.theme.GUNINevigationTheme
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.common.location.Location
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.navigation.base.ExperimentalPreviewMapboxNavigationAPI
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.base.route.NavigationRoute
import com.mapbox.navigation.base.route.NavigationRouterCallback
import com.mapbox.navigation.base.route.RouterFailure
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.directions.session.RoutesObserver
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp
import com.mapbox.navigation.core.lifecycle.MapboxNavigationObserver
import com.mapbox.navigation.core.lifecycle.requireMapboxNavigation
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import com.mapbox.navigation.ui.maps.camera.NavigationCamera
import com.mapbox.navigation.ui.maps.camera.data.MapboxNavigationViewportDataSource
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineApi
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineView
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineApiOptions
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineViewOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : ComponentActivity() {

    private var subLocationName: String = ""
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private lateinit var mapView: MapView
    private lateinit var viewportDataSource: MapboxNavigationViewportDataSource
    private lateinit var navigationCamera: NavigationCamera
    private lateinit var routeLineApi: MapboxRouteLineApi
    private lateinit var routeLineView: MapboxRouteLineView
    private val navigationLocationProvider = NavigationLocationProvider()

    private var origin: Point? = null
    private var destination: Point? = null
    private var routeRequested = false
    private var isPermissionGranted = false

    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true

            if (granted) {
                isPermissionGranted = true
                showMapComposable()
            } else {
                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subLocationName = intent.getStringExtra("subLocationName").orEmpty()
        longitude = intent.getDoubleExtra("longitude", 0.0)
        latitude = intent.getDoubleExtra("latitude", 0.0)

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            isPermissionGranted = true
            showMapComposable()
        } else {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun showMapComposable() {
        setContent {
            GUNINevigationTheme {
                ContentOnMapScreen(
                    onMapViewReady = {
                        mapView = it
                        initializeMapComponents()
                        moveToDestinationOnly()
                    },
                    subLocationName = subLocationName,
                    onStartNavigation = {
                        requestRouteIfReady()
                    },
                    onStopNavigation = {
                        clearRoutesAndResetCamera()
                    },
                )
            }
        }
    }

    private fun initializeMapComponents() {
        mapView.location.apply {
            setLocationProvider(navigationLocationProvider)
            locationPuck = createDefault2DPuck()
            enabled = true
        }

        viewportDataSource = MapboxNavigationViewportDataSource(mapView.mapboxMap)

        val pixelDensity = resources.displayMetrics.density
        viewportDataSource.followingPadding = EdgeInsets(
            180.0 * pixelDensity, 40.0 * pixelDensity,
            150.0 * pixelDensity, 40.0 * pixelDensity
        )

        navigationCamera = NavigationCamera(mapView.mapboxMap, mapView.camera, viewportDataSource)

        routeLineApi = MapboxRouteLineApi(MapboxRouteLineApiOptions.Builder().build())
        routeLineView = MapboxRouteLineView(MapboxRouteLineViewOptions.Builder(this).build())

        destination = Point.fromLngLat(longitude, latitude)

        val annotationManager = mapView.annotations.createPointAnnotationManager()

        val bitmap = BitmapFactory.decodeResource(
            resources,
            R.drawable.red_marker
        )

        val destinationMarker = PointAnnotationOptions()
            .withPoint(destination!!)
            .withIconImage(bitmap)
            .withIconSize(0.3)

        annotationManager.create(destinationMarker)
    }

    private fun moveToDestinationOnly() {
        destination?.let { dest ->
            val screenHeight = resources.displayMetrics.heightPixels.toDouble()
            val verticalOffset = screenHeight / 100.0

            mapView.camera.easeTo(
                CameraOptions.Builder()
                    .center(dest)
                    .zoom(15.0)
                    .bearing(0.0)
                    .pitch(0.0)
                    .padding(EdgeInsets(verticalOffset, 0.0, 0.0, 0.0))
                    .build(),
                MapAnimationOptions.mapAnimationOptions {
                    duration(1500)
                }
            )
        }
    }

    private fun clearRoutesAndResetCamera() {
        routeRequested = false

        mapboxNavigation.setNavigationRoutes(emptyList())

        mapView.mapboxMap.getStyle()?.let { style ->
            routeLineApi.clearRouteLine { result ->
                routeLineView.renderClearRouteLineValue(style, result)
                navigationCamera.requestNavigationCameraToIdle()
                viewportDataSource.clearRouteData()
                moveToDestinationOnly()
            }
        }
    }


    private val routesObserver = RoutesObserver { result ->
        if (result.navigationRoutes.isNotEmpty()) {
            routeLineApi.setNavigationRoutes(result.navigationRoutes) { drawResult ->
                mapView.mapboxMap.style?.let {
                    routeLineView.renderRouteDrawData(it, drawResult)
                }
            }

            viewportDataSource.onRouteChanged(result.navigationRoutes.first())
            viewportDataSource.evaluate()
            navigationCamera.requestNavigationCameraToOverview()
        }
    }

    private val locationObserver = object : LocationObserver {
        override fun onNewRawLocation(rawLocation: Location) {}

        override fun onNewLocationMatcherResult(result: LocationMatcherResult) {
            val enhanced = result.enhancedLocation
            navigationLocationProvider.changePosition(enhanced, result.keyPoints)

            viewportDataSource.onLocationChanged(enhanced)
            viewportDataSource.evaluate()

            if (routeRequested) {
                navigationCamera.requestNavigationCameraToFollowing()
            }

            if (origin == null) {
                origin = Point.fromLngLat(enhanced.longitude, enhanced.latitude)
            }
        }
    }

    @OptIn(ExperimentalPreviewMapboxNavigationAPI::class)
    private val mapboxNavigation: MapboxNavigation by requireMapboxNavigation(
        onInitialize = {
            MapboxNavigationApp.setup(
                NavigationOptions.Builder(this@MapActivity).build()
            )
        },
        onResumedObserver = object : MapboxNavigationObserver {
            @SuppressLint("MissingPermission")
            override fun onAttached(mapboxNavigation: MapboxNavigation) {
                if (!isPermissionGranted) return
                mapboxNavigation.registerRoutesObserver(routesObserver)
                mapboxNavigation.registerLocationObserver(locationObserver)
                mapboxNavigation.startTripSession()
            }

            override fun onDetached(mapboxNavigation: MapboxNavigation) {}
        }
    )

    @OptIn(ExperimentalPreviewMapboxNavigationAPI::class)
    private fun requestRouteIfReady() {
        val origin = this.origin
        val destination = this.destination

        if (origin != null && destination != null) {
            routeRequested = true

            mapboxNavigation.requestRoutes(
                RouteOptions.builder()
                    .applyDefaultNavigationOptions()
                    .coordinatesList(listOf(origin, destination))
                    .layersList(listOf(mapboxNavigation.getZLevel(), null))
                    .build(),
                object : NavigationRouterCallback {
                    override fun onRoutesReady(
                        routes: List<NavigationRoute>,
                        routerOrigin: String
                    ) {
                        mapboxNavigation.setNavigationRoutes(routes)
                    }

                    override fun onCanceled(routeOptions: RouteOptions, routerOrigin: String) {}
                    override fun onFailure(reasons: List<RouterFailure>, routeOptions: RouteOptions) {}
                }
            )
        }
    }
}
package com.devkt.guninevigation.screens.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.common.location.Location
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.animation.camera
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

class MapActivity : ComponentActivity() {

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
                initializeMapComponents()
            } else {
                isPermissionGranted = false
                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            isPermissionGranted = true
            initializeMapComponents()
        } else {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun initializeMapComponents() {
        if (!isPermissionGranted) return

        mapView = MapView(this)
        setContentView(mapView)

        mapView.mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(Point.fromLngLat(72.380941, 23.607099))
                .zoom(14.0)
                .build()
        )

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
            navigationCamera.requestNavigationCameraToFollowing()

            if (origin == null && !routeRequested) {
                origin = Point.fromLngLat(enhanced.longitude, enhanced.latitude)
                destination = Point.fromLngLat(72.380941, 23.607099)
                requestRouteIfReady()
            }
        }
    }

    @OptIn(ExperimentalPreviewMapboxNavigationAPI::class)
    private val mapboxNavigation: MapboxNavigation by requireMapboxNavigation(
        onInitialize = this::initNavigation,
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
    private fun initNavigation() {
        MapboxNavigationApp.setup(NavigationOptions.Builder(this).build())
    }

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

                    override fun onFailure(
                        reasons: List<RouterFailure>,
                        routeOptions: RouteOptions
                    ) {}
                }
            )
        }
    }
}

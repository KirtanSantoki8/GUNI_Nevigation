package com.devkt.guninevigation

import android.app.Application
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApp: Application() {
    override fun onCreate() {
        super.onCreate()

        MapboxNavigationApp.setup(
            NavigationOptions.Builder(this).build()
        )
    }
}
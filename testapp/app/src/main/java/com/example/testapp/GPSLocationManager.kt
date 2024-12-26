package com.example.testapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import com.google.android.gms.location.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class GPSLocationManager(private val context: Context) {

    private var getRate: Long = 10000//取得頻度(ms)
    private var minRate: Long = 5000//更新頻度(ms)

    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null

    interface MyLocationCallback {
        fun onLocationResult(location: Location?)
        fun onLocationError(error: String)
    }

    init {
        // LocationRequestの設定
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, getRate) // 10秒ごとに取得
            .setMinUpdateIntervalMillis(minRate) // 最小間隔5秒
            .build()
    }

    fun startLocationUpdates(callback: MyLocationCallback) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (context is MainActivity) {
                ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1000)
            }
            return
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    callback.onLocationResult(location)
                }
            }

            override fun onLocationAvailability(availability: LocationAvailability) {
                if (!availability.isLocationAvailable) {
                    callback.onLocationError("Location unavailable")
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest!!, locationCallback!!, null)
    }

    fun stopLocationUpdates() {
        locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
    }
}


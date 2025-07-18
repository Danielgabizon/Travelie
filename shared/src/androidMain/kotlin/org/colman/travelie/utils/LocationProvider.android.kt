package org.colman.travelie.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.colman.travelie.models.Location
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


actual class LocationProvider(private val context: Context) {

    actual suspend fun requestLocationPermission(): Boolean {
      return false // this is a placeholder, permissions handled in the UI layer
    }
    @SuppressLint("MissingPermission")
    actual suspend fun getCurrentLocation(): Location? = suspendCancellableCoroutine { continuation ->
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.getCurrentLocation(
            com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { location ->
            if (location != null) {
                continuation.resume(Location(location.latitude, location.longitude))
            } else {
                continuation.resumeWith(Result.failure(Exception("Location is null")))
            }
        }.addOnFailureListener { exception ->
            continuation.resumeWith(Result.failure(exception))
        }
    }


}
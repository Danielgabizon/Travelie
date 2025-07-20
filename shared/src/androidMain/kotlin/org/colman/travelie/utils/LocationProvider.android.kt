package org.colman.travelie.utils

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlin.coroutines.resume
import org.colman.travelie.models.Location
import org.colman.travelie.data.Result
import kotlin.coroutines.suspendCoroutine

actual class LocationProvider(private val context: Context) {

    actual suspend fun requestLocationPermission(): Boolean {
        // permission is handled in the UI layer
        return false // placeholder
    }

    @SuppressLint("MissingPermission")
    actual suspend fun getCurrentLocation(): Result<Location, LocationError> =
        suspendCoroutine { continuation ->
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { loc ->
                    if (loc != null) {
                        continuation.resume(Result.Success(Location(loc.latitude, loc.longitude)))
                    } else {
                        continuation.resume(
                            Result.Failure(LocationError("Location is null"))
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resume(
                        Result.Failure(
                            LocationError("Failed to get location: ${exception.message ?: "Unknown error"}")
                        )
                    )
                }
        }
}

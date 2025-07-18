package org.colman.travelie.utils
import org.colman.travelie.models.Location
expect class LocationProvider {
    suspend fun getCurrentLocation(): Location?
    suspend fun requestLocationPermission(): Boolean
}
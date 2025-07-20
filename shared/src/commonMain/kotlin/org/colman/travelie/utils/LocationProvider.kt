package org.colman.travelie.utils
import org.colman.travelie.data.Error
import org.colman.travelie.data.Result
import org.colman.travelie.models.Location


data class LocationError (
    override val message: String
) : Error

expect class LocationProvider {
    suspend fun getCurrentLocation(): Result<Location, LocationError>
    suspend fun requestLocationPermission(): Boolean
}
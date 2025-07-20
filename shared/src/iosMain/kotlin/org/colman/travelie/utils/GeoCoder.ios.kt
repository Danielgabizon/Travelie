package org.colman.travelie.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.withContext
import org.colman.travelie.data.Result
import platform.CoreLocation.CLGeocoder
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLPlacemark
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class GeoDecoder {
    actual suspend fun getCountryFromLocation(lat: Double, lon: Double): Result<String, GeoError> =
            suspendCoroutine { continuation ->
                val geocoder = CLGeocoder()
                val location = CLLocation(latitude = lat, longitude = lon)

                geocoder.reverseGeocodeLocation(location) { placemarks, error ->
                    if (error != null) {
                        continuation.resume(
                            Result.Failure(
                                GeoError("Failed to get country from location: ${error.localizedDescription}")
                            )
                        )
                    } else {
                        val CLPlacemark  = placemarks?.firstOrNull() as CLPlacemark?
                        if (CLPlacemark?.country != null) {
                            continuation.resume(Result.Success(CLPlacemark.country!!))
                        } else {
                            continuation.resume(
                                Result.Failure(
                                    GeoError("Country not found for coordinates: ($lat, $lon)")
                                )
                            )
                        }

                    }
                }
            }
        }

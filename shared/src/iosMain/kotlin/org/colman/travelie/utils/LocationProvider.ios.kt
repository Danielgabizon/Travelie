package org.colman.travelie.utils

import kotlinx.cinterop.ExperimentalForeignApi
import org.colman.travelie.models.Location
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocation
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.resume
import platform.CoreLocation.CLLocationCoordinate2D
import org.colman.travelie.data.Result

actual class LocationProvider {

    private var locationManager: CLLocationManager = CLLocationManager()

    init {
        // set accuracy for location updates
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
    }
    actual suspend fun requestLocationPermission(): Boolean =
        suspendCancellableCoroutine { continuation ->

            // Set up a delegate to handle the authorization status change
            val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
                override fun locationManager(manager: CLLocationManager, didChangeAuthorizationStatus: CLAuthorizationStatus) {
                    val authorized = didChangeAuthorizationStatus == kCLAuthorizationStatusAuthorizedWhenInUse ||
                            didChangeAuthorizationStatus == kCLAuthorizationStatusAuthorizedAlways
                    continuation.resume(authorized)
                    manager.delegate = null
                }
            }
            // Assign the delegate to the CLLocationManager instance
            locationManager.delegate = delegate
            // Request location permission
            locationManager.requestWhenInUseAuthorization()
        }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun getCurrentLocation(): Result<Location, LocationError> =
        suspendCancellableCoroutine { continuation ->

            // Set delegate to handle location updates
            val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {

                // Handle location updates
                override fun locationManager(
                    manager: CLLocationManager,
                    didUpdateLocations: List<*>
                ) {
                    val location = (didUpdateLocations.firstOrNull() as CLLocation?)
                    val coordinate = location?.coordinate as CLLocationCoordinate2D?
                    if (coordinate != null) {
                        continuation.resume(
                            Result.Success(
                                Location(
                                    coordinate.latitude,
                                    coordinate.longitude
                                )
                            )
                        )
                    } else {
                        continuation.resume(Result.Failure(LocationError("Failed to retrieve location coordinates.")))
                    }
                    manager.stopUpdatingLocation()
                    manager.delegate = null
                }

                // Handle errors
                override fun locationManager(
                    manager: CLLocationManager,
                    didFailWithError: NSError
                ) {
                    continuation.resume(Result.Failure(LocationError("Failed to retrieve location: ${didFailWithError.localizedDescription}")))
                    manager.delegate = null
                }
            }
            // Assign the delegate to the CLLocationManager instance
            locationManager.delegate = delegate
            // Start updating location
            locationManager.startUpdatingLocation()

        }
    }



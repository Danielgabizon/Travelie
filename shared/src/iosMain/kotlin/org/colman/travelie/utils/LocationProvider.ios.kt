package org.colman.travelie.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import org.colman.travelie.models.Location
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocation
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import platform.CoreLocation.CLLocationCoordinate2D


actual class LocationProvider {

    private var locationManager: CLLocationManager? = null

    actual suspend fun requestLocationPermission(): Boolean = withContext(Dispatchers.Main) {
        suspendCancellableCoroutine { continuation ->

            // Get LocationManager instance and set it to the class property
            val manager = CLLocationManager()
            locationManager = manager

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
            manager.delegate = delegate

            // Now we can request permission
            manager.requestWhenInUseAuthorization()
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun getCurrentLocation(): Location? = withContext(Dispatchers.Main) {
        suspendCancellableCoroutine { continuation ->

            // Get LocationManager instance and set it to the class property
            val manager = CLLocationManager()
            locationManager = manager

            // Set desired accuracy
            manager.desiredAccuracy = kCLLocationAccuracyBest

            // Set delegate to handle location updates
            val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {

                // Handle location updates
                override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
                    val loc = (didUpdateLocations.firstOrNull() as? CLLocation)
                    val coordinate = loc?.coordinate as? CLLocationCoordinate2D?
                    if (coordinate != null) {
                        continuation.resume(Location(coordinate.latitude,coordinate.longitude))
                    } else {
                        continuation.resumeWithException(IllegalStateException("Location is null"))
                    }
                    manager.stopUpdatingLocation()
                    manager.delegate = null
                }

                // Handle errors
                override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
                    continuation.resumeWithException(Exception(didFailWithError.localizedDescription))
                    manager.delegate = null
                }
            }

            // Assign the delegate to the CLLocationManager instance
            manager.delegate = delegate
            // Now we can start updating location
            manager.startUpdatingLocation()

        }

    }
}

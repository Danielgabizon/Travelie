package org.colman.travelie.utils
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.Locale
import org.colman.travelie.data.Result
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@RequiresApi(Build.VERSION_CODES.TIRAMISU)

actual class GeoDecoder(private val context: Context)  {
    private val geocoder: Geocoder = Geocoder(context, Locale.getDefault())
    actual suspend fun getCountryFromLocation(lat: Double, lon: Double): Result<String, GeoError> =
         suspendCoroutine { continuation ->

             geocoder.getFromLocation(lat, lon, 1, object : Geocoder.GeocodeListener {
                 override fun onGeocode(addresses: MutableList<Address>) {
                     val countryName = addresses.firstOrNull()?.countryName
                     if (countryName.isNullOrEmpty()) {
                         continuation.resume(
                             Result.Failure(
                                 GeoError(
                                     message = "Country name not found for coordinates: ($lat, $lon)"
                                 )
                             )
                         )
                     }
                     continuation.resume(Result.Success(countryName))
                 }

                 override fun onError(errorMessage: String?) {
                     continuation.resume(
                         Result.Failure(
                             GeoError(
                                 message = "Geocoding error: ${errorMessage ?: "Unknown error"}"
                             )
                         )
                     )
                 }
             })
         }
}


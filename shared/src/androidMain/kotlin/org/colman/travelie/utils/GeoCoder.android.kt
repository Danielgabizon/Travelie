package org.colman.travelie.utils

import android.content.Context
import android.location.Geocoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

actual class GeoDecoder(private val context: Context)  {
    actual suspend fun getCountryFromLocation(lat: Double, lon: Double): String? {
        return withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(lat, lon, 1)
                addresses?.firstOrNull()?.countryName
            } catch (e: Exception) {
                null
            }
        }
    }
}
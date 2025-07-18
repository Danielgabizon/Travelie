package org.colman.travelie.utils

expect class GeoDecoder {
    suspend fun getCountryFromLocation(lat: Double, lon: Double): String?
}
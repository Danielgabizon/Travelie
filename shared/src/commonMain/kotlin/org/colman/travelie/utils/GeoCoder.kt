package org.colman.travelie.utils
import org.colman.travelie.data.Error
import org.colman.travelie.data.Result


data class GeoError (
    override val message: String
) : Error
expect class GeoDecoder {
    suspend fun getCountryFromLocation(lat: Double, lon: Double): Result<String, GeoError>
}
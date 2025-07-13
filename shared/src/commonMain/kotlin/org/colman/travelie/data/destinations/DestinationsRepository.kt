package org.colman.travelie.data.destinations

import org.colman.travelie.data.Result
import org.colman.travelie.models.Destinations

interface DestinationsRepository {
    suspend fun getDestinations(): Result<Destinations,TDDBError>
}
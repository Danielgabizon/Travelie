package org.colman.travelie.data.destinations

import org.colman.travelie.data.Error
import org.colman.travelie.data.Result
import org.colman.travelie.models.Destinations

data class TDDBError (
    override val message: String
) : Error


class RemoteDestinationsRepository(): DestinationsRepository {
    override suspend fun getDestinations(): Result<Destinations, TDDBError> {
      return Result.Failure(
            TDDBError("Remote destinations repository not implemented")
        )
    }
}
package org.colman.travelie.data.destinations

import org.colman.travelie.data.Error
import org.colman.travelie.data.Result
import org.colman.travelie.models.Destinations

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.headers
import io.ktor.http.isSuccess
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.colman.travelie.models.Destination

data class TDDBError (
    override val message: String
) : Error

val apiKey = "286857182eef4c659f882c4b7b96f7319158eeac7ad454cafdbd787d7bc3d4b3"

class RemoteDestinationsRepository(
    private val client: HttpClient,
): DestinationsRepository {
    override suspend fun getDestinations(query: String): Result<Destinations, TDDBError> {
        return try {
            val response: HttpResponse = client.get("https://serpapi.com/search.json") {
                url {
                    parameters.append("engine", "google")
                    parameters.append("q", query)
                    parameters.append("api_key", apiKey) // <-- important!
                    parameters.append("language", "en-US")
                    parameters.append("page", "1")
                }

                headers {
                    accept(ContentType.Application.Json)
                }
            }
            if (!response.status.isSuccess()) {
                val errorText = response.bodyAsText() // get the response body content
                return Result.Failure(
                    TDDBError("Error fetching destinations: ${response.status.value} - $errorText"))
            } else {
                val destinationsResponse: DestinationsResponse = response.body()
                val destinationsList = destinationsResponse.popularDestinations.destinations
                Result.Success(Destinations(items = destinationsList))
            }
        } catch (e: Exception) {
            Result.Failure(TDDBError("Network error: ${e.message ?: "Unknown error"}"))
        }

    }
}

@Serializable
data class DestinationsResponse(
    @SerialName("popular_destinations") val popularDestinations: PopularDestinations
)

@Serializable
data class PopularDestinations(
    val destinations: List<Destination>,
    @SerialName("show_more_link") val showMoreLink: String? = null
)

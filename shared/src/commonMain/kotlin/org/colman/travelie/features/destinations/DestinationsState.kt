package org.colman.travelie.features.destinations
import org.colman.travelie.features.UiState
import org.colman.travelie.models.Destinations

public sealed class DestinationsState: UiState {
    data object Loading: DestinationsState()
    data class Loaded(
        val destinations: Destinations
    ): DestinationsState()
    data class Error(
        var errorMessage: String
    ): DestinationsState()
}

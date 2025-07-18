package org.colman.travelie.features.destinations

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.colman.travelie.data.Result
import org.colman.travelie.models.Destinations
import org.colman.travelie.features.BaseViewModel
import org.colman.travelie.utils.GeoDecoder
import org.colman.travelie.utils.LocationProvider

class DestinationsViewModel(
    val useCases: DestinationsUseCases,
    private val locationProvider: LocationProvider,
    private val geoDecoder: GeoDecoder // you'll create this next
) : BaseViewModel<DestinationsState>() {

    private val _uiState: MutableStateFlow<DestinationsState> = MutableStateFlow(DestinationsState.Loading)
    override val uiState: StateFlow<DestinationsState> get() = _uiState


    fun searchByCurrentLocation() {
        scope.launch {
            val location = locationProvider.getCurrentLocation() ?: return@launch
            val country = geoDecoder.getCountryFromLocation(location.latitude, location.longitude)
            if (!country.isNullOrBlank()) {
                search(country)
            }
        }
    }

    fun search(query: String) {
        if (query.isBlank()) return

        scope.launch {
            _uiState.emit(DestinationsState.Loading)

            val result = useCases.getDestinations(query)

            when (result) {
                is Result.Success -> {
                    _uiState.emit(
                        DestinationsState.Loaded(
                            result.data ?: Destinations(emptyList())
                        )
                    )
                }
                is Result.Failure -> {
                    _uiState.emit(
                        DestinationsState.Error(
                            result.error?.message ?: "Unknown error"
                        )
                    )
                }
            }
        }
    }
}

package org.colman.travelie.features.destinations

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.colman.travelie.data.Result
import org.colman.travelie.domain.GetDestinations
import org.colman.travelie.models.Destinations
import org.colman.travelie.features.BaseViewModel

class DestinationsViewModel(
    val useCases: DestinationsUseCases
) : BaseViewModel<DestinationsState>() {

    private val _uiState: MutableStateFlow<DestinationsState> = MutableStateFlow(DestinationsState.Loading)
    override val uiState: StateFlow<DestinationsState> get() = _uiState

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

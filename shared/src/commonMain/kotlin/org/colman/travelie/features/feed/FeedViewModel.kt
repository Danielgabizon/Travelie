package org.colman.travelie.features.feed

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.colman.travelie.data.Result
import org.colman.travelie.features.BaseViewModel
import org.colman.travelie.utils.eventBus.Event
import org.colman.travelie.utils.eventBus.EventBus


class FeedViewModel(
    private val feedUseCases: FeedUseCases,
) : BaseViewModel<FeedState>() {
    private val _uiState: MutableStateFlow<FeedState> = MutableStateFlow(FeedState.Loading)
    override val uiState: StateFlow<FeedState> get() = _uiState

    init {
        getPosts()
        observeEvents()
    }

    private fun getPosts() {
        scope.launch {
            when (val result = feedUseCases.getPosts()) {
                is Result.Success -> _uiState.emit(FeedState.Loaded(result.data!!))
                is Result.Failure -> _uiState.emit(
                    FeedState.Error(
                        result.error?.message ?: "Unknown error"
                    )
                )
            }
        }
    }
    private fun observeEvents() {
        scope.launch {
            EventBus.collectEvents { event ->
                when (event) {
                    Event.PostUploaded, Event.CommentAdded -> {
                        _uiState.emit(FeedState.Loading)
                        getPosts()
                    }

                }
            }
        }
    }
}




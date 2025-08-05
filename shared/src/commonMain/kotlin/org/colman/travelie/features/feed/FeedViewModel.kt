package org.colman.travelie.features.feed

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.colman.travelie.data.Result
import org.colman.travelie.features.BaseViewModel


class FeedViewModel(
    private val feedUseCases: FeedUseCases,
) : BaseViewModel<FeedState>() {
    private val _uiState: MutableStateFlow<FeedState> = MutableStateFlow(FeedState.Loading)
    override val uiState: StateFlow<FeedState> get() = _uiState

    init {
        getPosts()
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
    fun refreshPosts() {
        scope.launch {
            _uiState.emit(FeedState.Loading)
            getPosts()
        }
    }
}




package org.colman.travelie.features.feed

import org.colman.travelie.features.UiState
import org.colman.travelie.models.Posts

public sealed class FeedState: UiState {
    data object Loading: FeedState()
    data class Loaded(
        val posts: Posts,
    ): FeedState()
    data class Error(
        var errorMessage: String
    ): FeedState()
}

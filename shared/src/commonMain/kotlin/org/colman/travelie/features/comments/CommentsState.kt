package org.colman.travelie.features.comments

import org.colman.travelie.features.UiState
import org.colman.travelie.models.Comments

public sealed class CommentsState: UiState {
    data object Loading: CommentsState()
    data class Loaded(
        val comments: Comments,
    ) : CommentsState()
    data class Error(var errorMessage: String): CommentsState()
}

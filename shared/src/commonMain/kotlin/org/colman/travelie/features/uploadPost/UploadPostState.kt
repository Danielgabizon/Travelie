package org.colman.travelie.features.uploadPost

import org.colman.travelie.features.UiState
import org.colman.travelie.models.Post

public sealed class UploadPostState: UiState {
    data object Loading: UploadPostState()
    data class Loaded(
        val post: Post?,
    ): UploadPostState()
    data class Error(
        var errorMessage: String
    ): UploadPostState()
}




package org.colman.travelie.features.profile

import org.colman.travelie.features.UiState
import org.colman.travelie.models.Posts

public sealed class ProfileState: UiState {
    data object Loading: ProfileState()
    data class Loaded(
        val userPosts: Posts?
    ): ProfileState()
    data class Error(
        var errorMessage: String
    ): ProfileState()
}


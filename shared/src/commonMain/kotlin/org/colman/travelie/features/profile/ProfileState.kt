package org.colman.travelie.features.profile

import org.colman.travelie.features.UiState
import org.colman.travelie.models.Posts
import org.colman.travelie.models.User

public sealed class ProfileState: UiState {
    data object Loading: ProfileState()
    data class Loaded(
        val user: User,
        val userPosts: Posts
    ): ProfileState()
    data class Error(
        var errorMessage: String
    ): ProfileState()
}


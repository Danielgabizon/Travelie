package org.colman.travelie.features.user

import org.colman.travelie.features.UiState
import org.colman.travelie.models.User


public sealed class UserState: UiState {
    data object Loading: UserState()
    data class Loaded(
        val user: User?,
    ): UserState()
    data class Error(
        var errorMessage: String
    ): UserState()
}
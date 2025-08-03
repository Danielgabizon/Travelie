package org.colman.travelie.features.auth

import org.colman.travelie.features.UiState
import org.colman.travelie.models.AuthUser
import org.colman.travelie.models.User

public sealed class AuthState: UiState {
    data object Loading: AuthState()
    data class Loaded(
        val user: User?
    ): AuthState()
    data class Error(
        var errorMessage: String
    ): AuthState()
}

package org.colman.travelie.features.register

import org.colman.travelie.features.UiState
import org.colman.travelie.models.User

public sealed class RegisterState: UiState {
    data object Idle : RegisterState()
    data object Loading : RegisterState()
    data class Loaded(val user: User) : RegisterState()
    data class Error(val errorMessage: String) : RegisterState()
}
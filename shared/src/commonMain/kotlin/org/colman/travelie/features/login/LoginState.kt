package org.colman.travelie.features.login

import org.colman.travelie.features.UiState
import org.colman.travelie.models.User

public sealed class LoginState: UiState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class Loaded(val user: User) : LoginState()
    data class Error(val errorMessage: String) : LoginState()
}
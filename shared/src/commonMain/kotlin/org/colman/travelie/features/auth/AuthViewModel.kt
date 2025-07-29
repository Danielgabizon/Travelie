package org.colman.travelie.features.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.colman.travelie.features.BaseViewModel
import org.colman.travelie.data.Result

class AuthViewModel(
    private val useCases: AuthUseCases
) : BaseViewModel<AuthState>() {
    private val _uiState: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Loaded(null))
    override val uiState: StateFlow<AuthState> get() = _uiState
    fun login(email: String, password: String) {
        scope.launch {
            _uiState.emit(AuthState.Loading)
            val result = useCases.login(email, password)
            when (result) {
                is Result.Success -> _uiState.emit(AuthState.Loaded(result.data))
                is Result.Failure -> _uiState.emit(
                    AuthState.Error(
                        result.error?.message ?: "Unknown error"
                    )
                )
            }
        }
    }

    fun register(email: String, password: String,
                 firstName: String, lastName: String, bio: String) {
        _uiState.value = AuthState.Loading
        scope.launch {
            when (val result = useCases.register(email, password,
                firstName, lastName, bio)) {
                is Result.Success -> _uiState.emit(AuthState.Loaded(result.data))
                is Result.Failure -> _uiState.emit(
                    AuthState.Error(
                        result.error?.message ?: "Unknown error"
                    )
                )
            }
        }
    }

    fun logout() {
        scope.launch {
            _uiState.emit(AuthState.Loading)
            when (val result = useCases.logout()) {
                is Result.Success -> _uiState.emit(AuthState.Loaded(null))
                is Result.Failure -> _uiState.emit(
                    AuthState.Error(
                        result.error?.message ?: "Logout failed"
                    )
                )
            }
        }
    }

}

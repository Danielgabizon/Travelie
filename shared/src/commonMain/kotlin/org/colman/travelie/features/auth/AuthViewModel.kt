package org.colman.travelie.features.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.colman.travelie.features.BaseViewModel
import org.colman.travelie.data.Result
import org.colman.travelie.data.firebase.AuthError
import org.colman.travelie.data.firebase.UserDBError
import org.colman.travelie.models.User

class AuthViewModel(
    private val authUseCases: AuthUseCases,
) : BaseViewModel<AuthState>() {
    private val _uiState: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Loaded(null))
    override val uiState: StateFlow<AuthState> get() = _uiState
    fun login(email: String, password: String) {
        scope.launch {
            _uiState.emit(AuthState.Loading)
            val result = authUseCases.login(email, password)
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
    fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        bio: String
    ) {
        _uiState.value = AuthState.Loading
        scope.launch {
            when (val authResult = authUseCases.register(email, password)) {
                is Result.Success -> {
                    val authUser = authResult.data!!

                    // saving the user in Firestore
                    when (val saveResult = authUseCases.saveUser(
                        User(
                            uid = authUser.uid,
                            email = authUser.email,
                            firstName = firstName,
                            lastName = lastName,
                            bio = bio
                        )
                    )) {
                        is Result.Success -> {
                            _uiState.emit(AuthState.Loaded(authUser))
                        }
                        is Result.Failure -> {
                            _uiState.emit(AuthState.Error("Failed to save user data: ${saveResult.error?.message}"))
                        }
                    }
                }

                is Result.Failure -> {
                    _uiState.emit(AuthState.Error(authResult.error?.message ?: "Unknown error"))
                }
            }
        }
    }


    fun logout() {
        scope.launch {
            _uiState.emit(AuthState.Loading)
            when (val result = authUseCases.logout()) {
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

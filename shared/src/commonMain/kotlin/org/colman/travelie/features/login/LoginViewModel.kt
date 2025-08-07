package org.colman.travelie.features.login

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.colman.travelie.utils.SessionManager
import org.colman.travelie.data.Result
import org.colman.travelie.features.BaseViewModel


class LoginViewModel(
    private val loginUseCases: LoginUseCases,
    private val sessionManager: SessionManager,
) : BaseViewModel<LoginState>() {
    private val _uiState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Idle)
    override val uiState: StateFlow<LoginState> get() = _uiState
    fun login(email: String, password: String) {
        scope.launch {
            _uiState.emit(LoginState.Loading)
            // login with Firebase Auth
            when (val result = loginUseCases.login(email, password)) {
                is Result.Success -> {
                    val authUser = result.data!!

                    // read user data from Firestore
                    when (val userResult = loginUseCases.getUserById(authUser.uid)) {
                        is Result.Success -> {
                            sessionManager.setUser(userResult.data) // store user in session manager
                            _uiState.emit(LoginState.Loaded(userResult.data!!))
                        }

                        is Result.Failure -> {
                            _uiState.emit(LoginState.Error(userResult.error?.message ?: "Failed to fetch user data")
                            )
                        }
                    }
                }

                is Result.Failure -> _uiState.emit(
                    LoginState.Error(
                        result.error?.message ?: "Unknown error"
                    )
                )
            }
        }
    }
}

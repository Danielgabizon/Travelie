package org.colman.travelie.features.register
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import org.colman.travelie.auth.SessionManager
import org.colman.travelie.features.BaseViewModel
import org.colman.travelie.data.Result
import org.colman.travelie.models.User
class RegisterViewModel(
    private val registerUseCases: RegisterUseCases,
    private val sessionManager: SessionManager
) : BaseViewModel<RegisterState>() {

    private val _uiState = MutableStateFlow<RegisterState>(RegisterState.Loaded(null))
    override val uiState: StateFlow<RegisterState> = _uiState

    fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        bio: String
    ) {
        scope.launch {
            _uiState.emit(RegisterState.Loading)

            // register with Firebase Auth
            when (val registerResult = registerUseCases.register(email, password)) {
                is Result.Success -> {
                    val authUser = registerResult.data!!

                    // create full user model
                    val newUser = User(
                        uid = authUser.uid,
                        email = authUser.email,
                        firstName = firstName,
                        lastName = lastName,
                        bio = bio
                    )


                    // save to Firestore
                    when (val saveResult = registerUseCases.saveUser(newUser)) {
                        is Result.Success -> {
                            sessionManager.setUser(saveResult.data) // store user in session manager
                            _uiState.emit(RegisterState.Loaded(saveResult.data))
                        }

                        is Result.Failure -> {
                            _uiState.emit(RegisterState.Error(saveResult.error?.message ?: "Failed to save user"))
                        }
                    }
                }

                is Result.Failure -> {
                    _uiState.emit(RegisterState.Error(registerResult.error?.message ?: "Registration failed"))
                }
            }
        }
    }
}
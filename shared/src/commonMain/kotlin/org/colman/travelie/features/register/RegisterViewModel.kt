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

    private val _uiState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    override val uiState: StateFlow<RegisterState> = _uiState

    fun register(
        email: String,
        password: String,
        username: String,
        firstName: String,
        lastName: String,
        bio: String,
        profileImageBytes:ByteArray? = null,
        profileImageContentType: String? = null
    ) {
        scope.launch {
            _uiState.emit(RegisterState.Loading)

            // register with Firebase Auth
            when (val registerResult = registerUseCases.register(email, password)) {
                is Result.Success -> {
                    val authUser = registerResult.data!!
                    val uid = authUser.uid

                    //  upload profile picture to Firebase Storage
                    val profileUrl = if (profileImageBytes != null) {
                        when (val upload = registerUseCases.uploadProfilePicture(
                            uid = uid,
                            username = username,
                            bytes = profileImageBytes,
                            contentType = profileImageContentType ?: "image/jpeg"
                        )) {
                            is Result.Success -> upload.data ?: ""
                            is Result.Failure -> ""
                        }
                    } else ""

                    // create User object
                    val newUser = User(
                        uid = uid,
                        email = authUser.email,
                        username = username,
                        firstName = firstName,
                        lastName = lastName,
                        bio = bio,
                        profilePicture = profileUrl
                    )

                    // save user to Firestore
                    when (val saveResult = registerUseCases.saveUser(newUser)) {
                        is Result.Success -> {
                            sessionManager.setUser(saveResult.data) // store user in session manager
                            _uiState.emit(RegisterState.Loaded(saveResult.data!!))
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
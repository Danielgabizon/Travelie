package org.colman.travelie.features.profile

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.colman.travelie.data.Result
import org.colman.travelie.features.BaseViewModel

class ProfileViewModel(
    private val profileUseCases: ProfileUseCases,
) : BaseViewModel<ProfileState>() {
    private val _uiState: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState.Loaded(null))
    override val uiState: StateFlow<ProfileState> get() = _uiState

    fun getUserDetails(uid: String) {
        scope.launch {
            _uiState.emit(ProfileState.Loading)
            val result = profileUseCases.getUserDetails(uid)
            when (result) {
                is Result.Success -> _uiState.emit(ProfileState.Loaded(result.data))
                is Result.Failure -> _uiState.emit(
                    ProfileState.Error(
                        result.error?.message ?: "Unknown error"
                    )
                )
            }
        }
    }


}

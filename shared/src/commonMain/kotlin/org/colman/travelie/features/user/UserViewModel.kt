package org.colman.travelie.features.user

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.colman.travelie.features.BaseViewModel
import org.colman.travelie.data.Result

class UserViewModel(
    private val userUseCases: UserUseCases,
) : BaseViewModel<UserState>() {
    private val _uiState: MutableStateFlow<UserState> = MutableStateFlow(UserState.Loaded(null))
    override val uiState: StateFlow<UserState> get() = _uiState

    fun getUserDetails(uid: String) {
        scope.launch {
            _uiState.emit(UserState.Loading)
            val result = userUseCases.getUserDetails(uid)
            when (result) {
                is Result.Success -> _uiState.emit(UserState.Loaded(result.data))
                is Result.Failure -> _uiState.emit(
                    UserState.Error(
                        result.error?.message ?: "Unknown error"
                    )
                )
            }
        }
    }


}

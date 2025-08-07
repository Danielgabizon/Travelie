package org.colman.travelie.features.logout

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.colman.travelie.utils.SessionManager
import org.colman.travelie.features.BaseViewModel
class LogoutViewModel(
    private val logoutUseCases: LogoutUseCases,
    private val sessionManager: SessionManager
) : BaseViewModel<LogoutState>() {

    private val _uiState = MutableStateFlow<LogoutState>(LogoutState.Idle)
    override val uiState: StateFlow<LogoutState> get() = _uiState

    fun logoutUser() {
        scope.launch {
            _uiState.emit(LogoutState.Loading)
            try {
                logoutUseCases.logout()
                sessionManager.clearSession()
                _uiState.emit(LogoutState.Loaded)
            } catch (e: Exception) {
                _uiState.emit(LogoutState.Error(e.message ?: "Logout failed"))
            }
        }
    }
}
package org.colman.travelie.features.logout

import org.colman.travelie.features.UiState
import org.colman.travelie.models.Posts
import org.colman.travelie.models.User

public sealed class LogoutState: UiState {
    data object Idle : LogoutState()
    data object Loading : LogoutState()
    data object Loaded : LogoutState()
    data class Error(
        var errorMessage: String
    ): LogoutState()
}
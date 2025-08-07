package org.colman.travelie.features.profile

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.colman.travelie.utils.SessionManager
import org.colman.travelie.data.Result
import org.colman.travelie.features.BaseViewModel
import org.colman.travelie.models.User
import org.colman.travelie.utils.RefreshEvents


class ProfileViewModel(
    private val profileUseCases: ProfileUseCases,
    sessionManager: SessionManager
) : BaseViewModel<ProfileState>() {
    private val _uiState: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState.Loading)
    override val uiState: StateFlow<ProfileState> get() = _uiState

    val user: StateFlow<User?> = sessionManager.currentUser


    init {
        loadUserPosts()
        observeRefreshTrigger()
    }

    private fun loadUserPosts() {
        scope.launch {
            if (user.value == null) {
                _uiState.emit(ProfileState.Error("User not logged in"))
                return@launch
            }
            when (val postsResult = profileUseCases.getPosts(user.value?.uid)) {
                is Result.Success -> {
                    _uiState.emit(ProfileState.Loaded(postsResult.data!!))
                }

                is Result.Failure -> {
                    _uiState.emit(
                        ProfileState.Error(
                            postsResult.error?.message ?: "Failed to load posts"
                        )
                    )
                }
            }
        }
    }
    private fun observeRefreshTrigger() {
        scope.launch {
            RefreshEvents.refreshProfile.collect {
                _uiState.emit(ProfileState.Loading)
                loadUserPosts()
            }
        }
    }
}




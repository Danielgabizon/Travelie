package org.colman.travelie.features.profile

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.colman.travelie.auth.SessionManager
import org.colman.travelie.data.Result
import org.colman.travelie.features.BaseViewModel
import org.colman.travelie.features.feed.FeedState
import org.colman.travelie.features.feed.FeedUseCases


class ProfileViewModel(
    private val profileUseCases: ProfileUseCases,
    private val sessionManager: SessionManager

) : BaseViewModel<ProfileState>() {
    private val _uiState: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState.Loading)
    override val uiState: StateFlow<ProfileState> get() = _uiState

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        scope.launch {
            val user = sessionManager.currentUser.value
            if (user == null) {
                _uiState.emit(ProfileState.Error("User not logged in"))
                return@launch
            }
            when (val postsResult = profileUseCases.getPosts(user.uid)) {
                is Result.Success -> {
                    _uiState.emit(ProfileState.Loaded(user, postsResult.data!!))
                }
                is Result.Failure -> {
                    _uiState.emit(ProfileState.Error(postsResult.error?.message ?: "Failed to load posts"))
                }
            }
        }
    }

    fun refreshUserPosts() {
        scope.launch {
            _uiState.emit(ProfileState.Loading)
            loadUserProfile()
        }
    }

}




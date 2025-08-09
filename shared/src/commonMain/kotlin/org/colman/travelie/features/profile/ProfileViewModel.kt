package org.colman.travelie.features.profile

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.colman.travelie.auth.SessionManager
import org.colman.travelie.data.Result
import org.colman.travelie.features.BaseViewModel
import org.colman.travelie.utils.eventBus.Event
import org.colman.travelie.utils.eventBus.EventBus


class ProfileViewModel(
    private val profileUseCases: ProfileUseCases,
    sessionManager: SessionManager
) : BaseViewModel<ProfileState>() {
    private val _uiState: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState.Loading)
    override val uiState: StateFlow<ProfileState> get() = _uiState

    val user = sessionManager.currentUser


    init {
        loadUserPosts()
        observeEvents()
    }

    private fun loadUserPosts() {
        scope.launch {
            val currentUser = user.value

            if (currentUser == null) {
                _uiState.emit(ProfileState.Error("User not logged in"))
                return@launch
            }
            when (val postsResult = profileUseCases.getPosts(currentUser.uid)) {
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
    private fun observeEvents() {
        scope.launch {
            EventBus.collectEvents { event ->
                when (event) {
                    Event.PostUploaded -> {
                        _uiState.emit(ProfileState.Loading)
                        loadUserPosts()
                    }
                    else -> {}
                }
            }
        }
    }
}




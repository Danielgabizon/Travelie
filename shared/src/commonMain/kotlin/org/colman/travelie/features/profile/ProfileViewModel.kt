package org.colman.travelie.features.profile

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.colman.travelie.data.Result
import org.colman.travelie.features.BaseViewModel
import org.colman.travelie.features.feed.FeedState
import org.colman.travelie.features.feed.FeedUseCases


class ProfileViewModel(
    private val profileUseCases: ProfileUseCases,
) : BaseViewModel<ProfileState>() {
    private val _uiState: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState.Loading)
    override val uiState: StateFlow<ProfileState> get() = _uiState

    fun getUserPosts(uid: String) {
        scope.launch {
            when (val result = profileUseCases.getPosts(uid)) {
                is Result.Success -> _uiState.emit(ProfileState.Loaded(result.data!!))
                is Result.Failure -> _uiState.emit(
                    ProfileState.Error(
                        result.error?.message ?: "Unknown error"
                    )
                )
            }
        }
    }
    fun refreshUserPosts(uid: String) {
        scope.launch {
            _uiState.emit(ProfileState.Loading)
            getUserPosts(uid)
        }
    }

}




package org.colman.travelie.features.uploadPost

import org.colman.travelie.models.Post


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.colman.travelie.auth.SessionManager
import org.colman.travelie.data.Result
import org.colman.travelie.features.BaseViewModel



class UploadPostViewModel(
    private val uploadPostUseCases: UploadPostUseCases,
    private val sessionManager: SessionManager
) : BaseViewModel<UploadPostState>() {

    private val _uiState: MutableStateFlow<UploadPostState> = MutableStateFlow(UploadPostState.LoadingUser)
    override val uiState: StateFlow<UploadPostState> get() = _uiState

    init {
        loadUserDetails()
    }

    private fun loadUserDetails() {
        scope.launch {
            val user = sessionManager.currentUser.value
            if (user == null) {
                _uiState.emit(UploadPostState.Error("User not logged in"))
                return@launch
            }
            _uiState.emit(UploadPostState.UserLoaded(user))
        }
    }


    fun uploadPost(
        description: String,
        imageUrl: String
    ) {
        scope.launch {

            _uiState.emit(UploadPostState.UploadingPost)

            val user = sessionManager.currentUser.value
            if (user == null) {
                _uiState.emit(UploadPostState.Error("User not logged in"))
                return@launch
            }

            val post = Post(
                uid = user.uid,
                creatorName = "${user.firstName} ${user.lastName}",
                creatorImageUrl = user.profilePicture.orEmpty(),
                description = description,
                imageUrl = imageUrl,
            )

            when (val result = uploadPostUseCases.createPost(post)) {
                is Result.Success -> _uiState.emit(
                    UploadPostState.PostUploaded(
                        user,
                        result.data!!
                    )
                )
                is Result.Failure -> _uiState.emit(
                    UploadPostState.Error(result.error?.message ?: "Unknown error")
                )
            }
        }
    }


}






package org.colman.travelie.features.uploadPost

import org.colman.travelie.models.Post


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.colman.travelie.utils.SessionManager
import org.colman.travelie.data.Result
import org.colman.travelie.features.BaseViewModel



class UploadPostViewModel(
    private val uploadPostUseCases: UploadPostUseCases,
    private val sessionManager: SessionManager
) : BaseViewModel<UploadPostState>() {

    private val _uiState: MutableStateFlow<UploadPostState> = MutableStateFlow(UploadPostState.Idle)
    override val uiState: StateFlow<UploadPostState> get() = _uiState

    val user = sessionManager.currentUser


    fun uploadPost(
        description: String,
        imageUrl: String
    ) {
        scope.launch {

            _uiState.emit(UploadPostState.Loading)

            val user = sessionManager.currentUser.value
            if (user == null) {
                _uiState.emit(UploadPostState.Error("User not logged in"))
                return@launch
            }

            val post = Post(
                uid = user.uid,
                creatorUsername = user.username,
                creatorImageUrl = user.profilePicture.orEmpty(),
                description = description,
                imageUrl = imageUrl,
            )

            when (val result = uploadPostUseCases.createPost(post)) {
                is Result.Success -> _uiState.emit(
                    UploadPostState.Loaded(
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






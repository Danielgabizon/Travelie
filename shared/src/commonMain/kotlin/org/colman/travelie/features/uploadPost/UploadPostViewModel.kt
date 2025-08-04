package org.colman.travelie.features.uploadPost

import org.colman.travelie.models.Post


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.colman.travelie.data.Result
import org.colman.travelie.features.BaseViewModel
import org.colman.travelie.models.User


class UploadPostViewModel(
    private val UploadPostUseCases: UploadPostUseCases,
) : BaseViewModel<UploadPostState>() {

    private val _uiState: MutableStateFlow<UploadPostState> =
        MutableStateFlow(UploadPostState.Loaded(null))
    override val uiState: StateFlow<UploadPostState> get() = _uiState

    fun uploadPost(
        user: User,
        description: String,
        imageUrl: String
    ) {
        scope.launch {

            _uiState.emit(UploadPostState.Loading)

            val post = Post(
                uid = user.uid,
                creatorName = "${user.firstName} ${user.lastName}",
                creatorImageUrl = user.profilePicture.orEmpty(),
                description = description,
                imageUrl = imageUrl
            )

            when (val result = UploadPostUseCases.createPost(post)) {
                is Result.Success -> _uiState.emit(UploadPostState.Loaded(result.data!!))
                is Result.Failure -> _uiState.emit(
                    UploadPostState.Error(result.error?.message ?: "Unknown error")
                )
            }
        }
    }


}






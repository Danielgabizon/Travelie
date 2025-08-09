package org.colman.travelie.features.uploadPost

import org.colman.travelie.models.Post
import org.colman.travelie.utils.eventBus.Event


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.colman.travelie.auth.SessionManager
import org.colman.travelie.data.Result
import org.colman.travelie.features.BaseViewModel
import org.colman.travelie.utils.eventBus.EventBus
import kotlin.uuid.Uuid
import kotlin.uuid.ExperimentalUuidApi


class UploadPostViewModel(
    private val uploadPostUseCases: UploadPostUseCases,
    sessionManager: SessionManager
) : BaseViewModel<UploadPostState>() {

    private val _uiState: MutableStateFlow<UploadPostState> = MutableStateFlow(UploadPostState.Idle)
    override val uiState: StateFlow<UploadPostState> get() = _uiState

    val user = sessionManager.currentUser

    @OptIn(ExperimentalUuidApi::class)
    fun uploadPost(
        description: String,
        postImageBytes:ByteArray? = null,
        postImageContentType: String? = null
    ) {
        scope.launch {
            val currentUser = user.value
            _uiState.emit(UploadPostState.Loading)

            if (currentUser == null) {
                _uiState.emit(UploadPostState.Error("User not logged in"))
                return@launch
            }

            val postId = Uuid.random().toString()

            val postUrl = if (postImageBytes != null) {
                when (val upload = uploadPostUseCases.uploadPostPicture(
                    postId = postId,
                    uid = currentUser.uid,
                    username = currentUser.username,
                    bytes = postImageBytes,
                    contentType = postImageContentType ?: "image/jpeg"
                )) {
                    is Result.Success -> upload.data ?: ""
                    is Result.Failure -> ""
                }
            } else ""

            val post = Post(
                postId = postId,
                uid = currentUser.uid,
                creatorUsername = currentUser.username,
                creatorImageUrl = currentUser.profilePicture.orEmpty(),
                description = description,
                imageUrl = postUrl
                ,
            )

            when (val result = uploadPostUseCases.createPost(post)) {
                is Result.Success -> {
                    _uiState.emit(UploadPostState.Loaded(result.data!!))
                    // notify that the post has been uploaded
                    EventBus.emit(Event.PostUploaded)
                }
                is Result.Failure -> _uiState.emit(
                    UploadPostState.Error(result.error?.message ?: "Unknown error")
                )
            }
        }
    }


}






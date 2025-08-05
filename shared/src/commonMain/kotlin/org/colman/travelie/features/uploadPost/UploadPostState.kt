package org.colman.travelie.features.uploadPost

import org.colman.travelie.features.UiState
import org.colman.travelie.models.Post
import org.colman.travelie.models.User

public sealed class UploadPostState: UiState {
    data object LoadingUser : UploadPostState()
    data class UserLoaded(val user: User) : UploadPostState()

    data object UploadingPost : UploadPostState()
    data class PostUploaded(val user: User, val post: Post) : UploadPostState()

    data class Error(val errorMessage: String) : UploadPostState()
}



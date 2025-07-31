package org.colman.travelie.features.post

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.colman.travelie.features.BaseViewModel
import org.colman.travelie.models.post

class PostViewModel(
    private val postUseCases: PostUseCases
) : BaseViewModel<PostState>() {

//    private val _uiState = MutableStateFlow<PostState>(PostState.Idle)
    override val uiState: StateFlow<PostState> = _uiState

    fun loadPosts() {
        scope.launch {
            _uiState.value = PostState.Loading
            val result = runCatching { postUseCases.getPosts()() }
            _uiState.value = result.fold(
                onSuccess = { PostState.Loaded(it) },
                onFailure = { PostState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun submitPost(post: post) {
        scope.launch {
            _uiState.value = PostState.Loading
            val result = runCatching { postUseCases.addPost(post) }
            _uiState.value = result.fold(
                onSuccess = { loadPosts() }, // Refresh after add
                onFailure = { PostState.Error(it.message ?: "Could not add post") }
            )
        }
    }
}

// PostState.kt
package org.colman.travelie.features.post

import org.colman.travelie.features.UiState
import org.colman.travelie.models.Post

sealed class PostState :UiState{
    object Loading : PostState()
    data class Loaded(val posts: List<Post>) : PostState()
    data class Error(val message: String) : PostState()
}

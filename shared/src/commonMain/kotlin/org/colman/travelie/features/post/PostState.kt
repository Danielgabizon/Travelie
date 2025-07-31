// PostState.kt
package org.colman.travelie.features.post

import org.colman.travelie.models.post

sealed class PostState {
    object Loading : PostState()
    data class Loaded(val posts: List<post>) : PostState()
    data class Error(val message: String) : PostState()
}

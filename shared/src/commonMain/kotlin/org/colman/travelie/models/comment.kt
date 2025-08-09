package org.colman.travelie.models

import kotlinx.serialization.Serializable

@Serializable
data class Comment (
    val commentId: String ="",
    val postId: String,
    val uid: String,
    val username: String,
    val userImageUrl: String,
    val content: String,
)
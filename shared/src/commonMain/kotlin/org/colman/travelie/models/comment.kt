package org.colman.travelie.models

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val commentId: String,
    val postId: String,
    val userName: String,
    val userProfileUrl: String? = null,
    val text: String,
    val timestamp: Long
)
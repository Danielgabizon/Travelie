package org.colman.travelie.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Post(
    val postId: String,
    val uid: String,
    val creatorUsername: String,
    val creatorImageUrl: String,
    val description: String,
    val imageUrl: String,
    val commentCount: Int = 0

)
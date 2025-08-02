package org.colman.travelie.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Post(
    val postId: String,
    val uid: String,
    val creatorName: String,
    val creatorImageUrl: String,
    val description: String,
    val imageUrl: String,
)
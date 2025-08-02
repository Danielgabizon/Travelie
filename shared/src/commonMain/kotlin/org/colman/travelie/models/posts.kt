package org.colman.travelie.models

import kotlinx.serialization.Serializable

@Serializable
data class Posts (
    val items: List<Post>
)
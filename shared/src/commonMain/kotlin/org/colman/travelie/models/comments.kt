package org.colman.travelie.models

import kotlinx.serialization.Serializable

@Serializable
data class Comments (
    val items: List<Comment>
)
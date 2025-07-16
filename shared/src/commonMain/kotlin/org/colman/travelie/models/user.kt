package org.colman.travelie.models
import kotlinx.serialization.Serializable

@Serializable
data class User (
    val uid: String,
    val email: String,
    val displayName: String? = null,
)
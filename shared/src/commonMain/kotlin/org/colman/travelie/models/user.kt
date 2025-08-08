package org.colman.travelie.models
import kotlinx.serialization.Serializable

@Serializable
data class User (
    val uid: String,
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val bio: String,
    val profilePicture: String,
)
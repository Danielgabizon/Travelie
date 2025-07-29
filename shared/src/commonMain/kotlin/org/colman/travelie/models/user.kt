package org.colman.travelie.models
import kotlinx.serialization.Serializable

@Serializable
data class User (
    val uid: String,
    val email: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val bio: String? = null,
    val profilePicture: String? = null,

)
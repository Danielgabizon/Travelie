package org.colman.travelie.models
import kotlinx.serialization.Serializable

@Serializable
data class post(
    val postId: String,
    val userId: String,
    val userName: String,               // For display without extra query
    val userProfileUrl: String? = null, // Optional avatar
    val text: String,
    val imageUrl: String? = null,
    val timestamp: Long,

    // 🔁 Interaction
    val likedBy: List<String> = emptyList(),
    val commentCount: Int = 0,

    // 📍 GPS Integration
    val latitude: Double? = null,
    val longitude: Double? = null,
    val locationName: String? = null,

)

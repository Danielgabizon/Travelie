package org.colman.travelie.utils.Extentions

import org.colman.travelie.models.User

fun Map<String, Any?>.toUser(): User {
    return User(
        uid = this["uid"] as? String ?: "",
        email = this["email"] as? String ?: "",
        firstName = this["firstName"] as? String,
        lastName = this["lastName"] as? String,
        bio = this["bio"] as? String,
        profilePicture = this["profilePicture"] as? String
    )
}
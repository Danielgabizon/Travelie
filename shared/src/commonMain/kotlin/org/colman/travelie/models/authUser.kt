package org.colman.travelie.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthUser (
    val uid: String,
    val email: String,
)
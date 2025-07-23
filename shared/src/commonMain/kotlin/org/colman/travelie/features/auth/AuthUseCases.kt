package org.colman.travelie.features.auth

import org.colman.travelie.domain.Auth.Login
import org.colman.travelie.domain.Auth.Logout
import org.colman.travelie.domain.Auth.Register

data class AuthUseCases(
    val login: Login,
    val register: Register,
    val logout: Logout,
)
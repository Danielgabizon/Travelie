package org.colman.travelie.features.auth

import org.colman.travelie.domain.Auth.Login
import org.colman.travelie.domain.Auth.Logout
import org.colman.travelie.domain.Auth.Register
import org.colman.travelie.domain.User.SaveUser

data class AuthUseCases(
    val register: Register,
    val saveUser: SaveUser,
    val login: Login,
    val logout: Logout,
)
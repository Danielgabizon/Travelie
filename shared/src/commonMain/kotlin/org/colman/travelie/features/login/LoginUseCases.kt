package org.colman.travelie.features.login

import org.colman.travelie.domain.Auth.Login
import org.colman.travelie.domain.User.GetUserById

class LoginUseCases (
    val login: Login,
    val getUserById: GetUserById
)
package org.colman.travelie.features.register

import org.colman.travelie.domain.Auth.Register
import org.colman.travelie.domain.User.GetUserById
import org.colman.travelie.domain.User.SaveUser


class RegisterUseCases (
    val register: Register,
    val saveUser: SaveUser
)
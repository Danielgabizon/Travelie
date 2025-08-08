package org.colman.travelie.features.register

import org.colman.travelie.domain.Auth.Register
import org.colman.travelie.domain.User.GetUserById
import org.colman.travelie.domain.User.SaveUser
import org.colman.travelie.domain.User.UploadProfilePicture


data class RegisterUseCases(
    val register: Register,
    val saveUser: SaveUser,
    val uploadProfilePicture: UploadProfilePicture
)


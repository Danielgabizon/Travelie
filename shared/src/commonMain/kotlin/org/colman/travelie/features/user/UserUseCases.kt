package org.colman.travelie.features.user

import org.colman.travelie.domain.User.GetUserDetails
import org.colman.travelie.domain.User.SaveUser

data class UserUseCases (
    val saveUser: SaveUser,
    val getUserDetails: GetUserDetails,
)
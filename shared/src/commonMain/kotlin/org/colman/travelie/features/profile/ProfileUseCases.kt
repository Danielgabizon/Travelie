package org.colman.travelie.features.profile

import org.colman.travelie.domain.User.GetUserDetails

data class ProfileUseCases (
    val getUserDetails: GetUserDetails,
)
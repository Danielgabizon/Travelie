package org.colman.travelie.domain.User

import org.colman.travelie.data.firebase.FirebaseRepository
import org.colman.travelie.models.User


class GetUserDetails(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(uid: String) = firebaseRepository.getUserDetails(uid)
}
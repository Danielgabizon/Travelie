package org.colman.travelie.domain.User

import org.colman.travelie.data.firebase.FirebaseRepository

class GetUserById(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(uid: String) = firebaseRepository.getUserById(uid)
}
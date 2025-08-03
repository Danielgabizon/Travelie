package org.colman.travelie.domain.User

import org.colman.travelie.data.firebase.FirebaseRepository


class GetUser(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(uid: String) = firebaseRepository.getUser(uid)
}
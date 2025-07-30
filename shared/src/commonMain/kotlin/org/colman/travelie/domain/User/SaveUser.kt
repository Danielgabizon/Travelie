package org.colman.travelie.domain.User

import org.colman.travelie.data.firebase.FirebaseRepository
import org.colman.travelie.models.User

class SaveUser(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(user: User) = firebaseRepository.saveUser(user)
}
package org.colman.travelie.domain.Auth

import org.colman.travelie.data.firebase.FirebaseRepository

class GetCurrentUser (private val firebaseRepository: FirebaseRepository) {
        suspend operator fun invoke() = firebaseRepository.getCurrentUser()
    }

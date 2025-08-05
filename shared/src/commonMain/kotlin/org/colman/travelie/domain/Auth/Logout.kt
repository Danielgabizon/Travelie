package org.colman.travelie.domain.Auth

import org.colman.travelie.auth.SessionManager
import org.colman.travelie.data.firebase.FirebaseRepository

class Logout(
    private val firebaseRepository: FirebaseRepository,
) {
    suspend operator fun invoke() {
        firebaseRepository.logout()
    }
}
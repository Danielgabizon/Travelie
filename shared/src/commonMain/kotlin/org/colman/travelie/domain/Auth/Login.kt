package org.colman.travelie.domain.Auth

import org.colman.travelie.data.firebase.FirebaseRepository

class Login(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(email: String, password: String) = firebaseRepository.login(email, password)
}
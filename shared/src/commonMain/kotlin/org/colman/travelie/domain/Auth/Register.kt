package org.colman.travelie.domain.Auth

import org.colman.travelie.data.firebase.FirebaseRepository

class Register(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(email: String, password: String, firstName:String ,lastName:String,bio:String) =
        firebaseRepository.register(email, password, firstName, lastName, bio)
}
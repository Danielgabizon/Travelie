package org.colman.travelie.domain.Post

import org.colman.travelie.data.firebase.FirebaseRepository

class GetPosts (
    private val firebaseRepository: FirebaseRepository
){
    suspend operator fun invoke() = firebaseRepository.getPosts()

}
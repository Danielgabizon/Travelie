package org.colman.travelie.domain.Post

import org.colman.travelie.data.firebase.FirebaseRepository
import org.colman.travelie.models.Post

class CreatePost (
    private val firebaseRepository: FirebaseRepository
){
    suspend operator fun invoke(post:Post) = firebaseRepository.createPost(post)

}
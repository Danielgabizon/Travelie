package org.colman.travelie.domain.post

import org.colman.travelie.data.firebase.FirebaseRepository
import org.colman.travelie.models.post

class AddPost(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(post: post) = firebaseRepository.addPost(post)
}

//package org.colman.travelie.domain.Post
//
//
//import org.colman.travelie.data.firebase.FirebaseRepository
//
//class AddPost(private val firebaseRepository: FirebaseRepository) {
//    suspend operator fun invoke() = firebaseRepository.addPost()
//}
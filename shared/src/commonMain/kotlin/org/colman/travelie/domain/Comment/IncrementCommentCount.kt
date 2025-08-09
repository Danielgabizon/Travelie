package org.colman.travelie.domain.Comment

import org.colman.travelie.data.firebase.FirebaseRepository

class IncrementCommentCount (
    private val firebaseRepository: FirebaseRepository
){
    suspend operator fun invoke(postId: String) = firebaseRepository.incrementCommentCount(postId)
}
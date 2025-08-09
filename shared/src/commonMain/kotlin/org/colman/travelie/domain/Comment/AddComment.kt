package org.colman.travelie.domain.Comment

import org.colman.travelie.data.firebase.FirebaseRepository
import org.colman.travelie.models.Comment

class AddComment (
    private val firebaseRepository: FirebaseRepository
){
    suspend operator fun invoke(comment: Comment) = firebaseRepository.addComment(comment)
}
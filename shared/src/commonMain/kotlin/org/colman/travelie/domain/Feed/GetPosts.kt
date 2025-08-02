package org.colman.travelie.domain.Feed

import org.colman.travelie.data.destinations.DestinationsRepository
import org.colman.travelie.data.firebase.FirebaseRepository

class GetPosts (
    private val firebaseRepository: FirebaseRepository
){
    suspend operator fun invoke() = firebaseRepository.getPosts()

}
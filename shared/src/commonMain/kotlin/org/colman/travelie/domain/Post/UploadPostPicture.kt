package org.colman.travelie.domain.Post

import org.colman.travelie.data.Result
import org.colman.travelie.data.firebase.FirebaseRepository
import org.colman.travelie.data.firebase.StorageError

class UploadPostPicture(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(  postId:String, uid: String, username:String,
                                  bytes: ByteArray,
                                  contentType: String): Result<String, StorageError> {
        return firebaseRepository.uploadPostPicture(postId,uid,username, bytes, contentType)
    }
}

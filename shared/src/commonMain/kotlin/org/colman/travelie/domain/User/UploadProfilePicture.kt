package org.colman.travelie.domain.User

import org.colman.travelie.data.firebase.FirebaseRepository
import org.colman.travelie.data.firebase.StorageError
import org.colman.travelie.data.Result

class UploadProfilePicture(private val firebaseRepository: FirebaseRepository) {
    suspend operator fun invoke(  uid: String, username:String,
                                  bytes: ByteArray,
                                  contentType: String): Result<String, StorageError> {
        return firebaseRepository.uploadProfilePicture(uid,username, bytes, contentType)
    }
}

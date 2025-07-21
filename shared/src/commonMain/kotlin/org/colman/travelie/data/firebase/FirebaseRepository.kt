package org.colman.travelie.data.firebase
import org.colman.travelie.data.Result
import org.colman.travelie.models.User

interface FirebaseRepository {
    suspend fun login(email: String, password: String): Result<User, AuthError>
    suspend fun register(email: String, password: String): Result<User, AuthError>
    suspend fun logout(): Result<Unit, AuthError>
    suspend fun getCurrentUser(): Result<User, AuthError>

}
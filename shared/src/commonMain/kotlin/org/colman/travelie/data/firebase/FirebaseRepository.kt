package org.colman.travelie.data.firebase
import org.colman.travelie.data.Result
import org.colman.travelie.models.AuthUser
import org.colman.travelie.models.User
import org.colman.travelie.models.Post

interface FirebaseRepository {
    // Authentication methods
    suspend fun login(email: String, password: String): Result<AuthUser, AuthError>
    suspend fun register(email: String, password: String): Result<AuthUser, AuthError>
    suspend fun logout(): Result<Unit, AuthError>
    // User management methods
    suspend fun saveUser(user: User): Result<User, UserError>
    suspend fun getUserDetails(uid: String): Result<User, UserError>
    //Post methods
    suspend fun addPost(post: Post): Result<Unit, PostError>


}
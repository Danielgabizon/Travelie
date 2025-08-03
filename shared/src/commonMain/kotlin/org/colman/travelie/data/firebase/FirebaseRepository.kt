package org.colman.travelie.data.firebase
import org.colman.travelie.data.Result
import org.colman.travelie.models.AuthUser
import org.colman.travelie.models.Post
import org.colman.travelie.models.Posts
import org.colman.travelie.models.User

interface FirebaseRepository {
    // Authentication methods
    suspend fun login(email: String, password: String): Result<AuthUser, AuthError>
    suspend fun register(email: String, password: String): Result<AuthUser, AuthError>
    suspend fun logout(): Result<Unit, AuthError>

    // User management methods
    suspend fun saveUser(user: User): Result<User, UserDBError>
    suspend fun getUser(uid: String): Result<User, UserDBError>

    // Post management methods
    suspend fun getPosts(): Result<Posts, PostDBError>
    suspend fun createPost(post: Post): Result<Post, PostDBError>


}
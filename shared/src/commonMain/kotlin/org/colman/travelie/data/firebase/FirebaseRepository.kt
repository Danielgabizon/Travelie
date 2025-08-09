package org.colman.travelie.data.firebase
import org.colman.travelie.data.Result
import org.colman.travelie.models.AuthUser
import org.colman.travelie.models.Comment
import org.colman.travelie.models.Comments
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
    suspend fun getUserById(uid: String): Result<User, UserDBError>
    suspend fun uploadProfilePicture(uid: String, username: String, bytes: ByteArray, contentType: String): Result<String, StorageError>

    // Post management methods
    suspend fun getPosts(uid: String?): Result<Posts, PostDBError>
    suspend fun createPost(post: Post): Result<Post, PostDBError>
    suspend fun uploadPostPicture(postId:String, uid: String, username: String, bytes: ByteArray, contentType: String): Result<String, StorageError>

    // Comment management methods
    suspend fun getComments(postId: String): Result<Comments, CommentDbError>
    suspend fun addComment(comment: Comment): Result<Comment, CommentDbError>
    suspend fun incrementCommentCount(postId: String): Result<Unit, CommentDbError>

}
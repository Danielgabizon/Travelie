package org.colman.travelie.data.firebase

import org.colman.travelie.models.User
import org.colman.travelie.data.Error
import org.colman.travelie.data.Result
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.FieldValue
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.storage.storage
import dev.gitlive.firebase.storage.*
import dev.gitlive.firebase.storage.Data


import kotlin.uuid.ExperimentalUuidApi

import org.colman.travelie.models.AuthUser
import org.colman.travelie.models.Comment
import org.colman.travelie.models.Comments
import org.colman.travelie.models.Post
import org.colman.travelie.models.Posts
import kotlin.uuid.Uuid

expect class PlatformData(bytes: ByteArray) {
    fun toGitLiveData(): Data
}

data class AuthError(
    override val message: String
) : Error

data class UserDBError(
    override val message: String
) : Error
data class PostDBError(
    override val message: String
) : Error
data class CommentDbError(
    override val message: String
) : Error

data class StorageError(
    override val message: String
) : Error





class RemoteFirebaseRepository : FirebaseRepository {
    private val auth = Firebase.auth
    private val firestore = Firebase.firestore
    private val storage = Firebase.storage

    private val usersCollection = firestore.collection("users")
    private val postsCollection = firestore.collection("posts")
    private val commentsCollection = firestore.collection("comments")



    /* Authentication methods */
    override suspend fun login(email: String, password: String): Result<AuthUser, AuthError> {
        return try {
            auth.signInWithEmailAndPassword(email, password)

            val firebaseUser = auth.currentUser ?: return Result.Failure(AuthError("User is null"))

            val authUser = AuthUser(
                uid = firebaseUser.uid,
                email = firebaseUser.email.orEmpty()
            )

            Result.Success(authUser)

        } catch (e: Exception) {
            Result.Failure(AuthError("Login error: ${e.message ?: "Unknown error"}"))
        }
    }

    override suspend fun register(email: String, password: String): Result<AuthUser, AuthError> {
        return try {

            auth.createUserWithEmailAndPassword(email, password)
            val firebaseUser = auth.currentUser ?: return Result.Failure(AuthError("User is null"))

            val authUser = AuthUser(
                uid = firebaseUser.uid,
                email = firebaseUser.email.orEmpty()
            )

            Result.Success(authUser)

        } catch (e: Exception) {
            Result.Failure(AuthError("Registration error: ${e.message ?: "Unknown error"}"))
        }
    }


    override suspend fun logout(): Result<Unit, AuthError> {
        return try {
            auth.signOut()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(AuthError("Logout error: ${e.message ?: "Unknown error"}"))
        }
    }

    /* User methods */

    override suspend fun saveUser(user: User): Result<User, UserDBError> {
        return try {
            usersCollection
                .document(user.uid)
                .set(user)
            Result.Success(user)
        } catch (e: Exception) {
            Result.Failure(UserDBError("Save user error: ${e.message ?: "Unknown error"}"))
        }
    }
    override suspend fun uploadProfilePicture(
        uid: String,
        username: String,
        bytes: ByteArray,
        contentType: String
    ): Result<String, StorageError> = try {

        val fileName = "profile_${uid}.bin"
        val ref = storage.reference.child("users/$username/$fileName")

        val data = PlatformData(bytes).toGitLiveData()
        val meta = storageMetadata {
            this.contentType = contentType
            setCustomMetadata("uid", uid)
            setCustomMetadata("username", username)
        }


        ref.putData(data, meta)
        val url = ref.getDownloadUrl()

        Result.Success(url)
    } catch (e: Exception) {
        Result.Failure(StorageError("Upload error: ${e.message ?: "Unknown error"}"))
    }


    override suspend fun getUserById(uid: String): Result<User, UserDBError> {
        return try {
            val document =
                usersCollection
                    .document(uid)
                    .get()

            if (document.exists) {
                val user = document.data<User>()
                Result.Success(user)
            } else {
                Result.Failure(UserDBError("User not found"))
            }
        } catch (e: Exception) {
            Result.Failure(UserDBError("Get user details error: ${e.message ?: "Unknown error"}"))
        }
    }


    /* Post methods */

    override suspend fun getPosts(uid: String?): Result<Posts, PostDBError> {
        return try {
            val snapshot = if (uid != null) {
                postsCollection
                    .where { "uid" equalTo uid }
                    .get()
            } else {
                postsCollection
                    .get()
            }
            val posts = snapshot.documents.map { it.data<Post>() }
            Result.Success(Posts(items = posts))

        } catch (e: Exception) {
            Result.Failure(PostDBError("Get posts error: ${e.message ?: "Unknown error"}"))
        }
    }
    override suspend fun createPost(post: Post): Result<Post, PostDBError> {
        return try {
            postsCollection
                .document(post.postId)
                .set(post)
            Result.Success(post)
        } catch (e: Exception) {
            Result.Failure(PostDBError("Create post error: ${e.message ?: "Unknown error"}"))
        }
    }
    override suspend fun uploadPostPicture(
        postId: String,
        uid: String,
        username: String,
        bytes: ByteArray,
        contentType: String
    ): Result<String, StorageError> = try {

        val fileName = "post_${postId}.bin"
        val ref = storage.reference.child("posts/$username/$fileName")

        val data = PlatformData(bytes).toGitLiveData()
        val meta = storageMetadata {
            this.contentType = contentType
            setCustomMetadata("uid", uid)
            setCustomMetadata("postId", postId)
            setCustomMetadata("username", username)
        }

        ref.putData(data, meta)
        val url = ref.getDownloadUrl()

        Result.Success(url)

    } catch (e: Exception) {
        Result.Failure(StorageError("Upload error: ${e.message ?: "Unknown error"}"))
    }

    /* Comment methods  */
    override suspend fun getComments(postId: String): Result<Comments,CommentDbError> {
            return try {
                val snapshot = commentsCollection
                    .where { "postId" equalTo postId }
                    .get()

                val comments = snapshot.documents.map { it.data<Comment>() }
                Result.Success(Comments(items = comments))

            } catch (e: Exception) {
                Result.Failure(CommentDbError("Get comments error: ${e.message ?: "Unknown error"}"))
            }
    }


    override suspend fun addComment(comment: Comment): Result<Comment, CommentDbError> {
        return try {
            commentsCollection
                .document(comment.commentId)
                .set(comment)
            Result.Success(comment)
        } catch (e: Exception) {
            Result.Failure(CommentDbError("Add comment error: ${e.message ?: "Unknown error"}"))
        }
    }
    override suspend fun incrementCommentCount(postId: String): Result<Unit, CommentDbError> {
        return try {
            val postRef = postsCollection.document(postId)
            postRef.update(mapOf("commentCount" to FieldValue.increment(1)))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(CommentDbError("Increment comment count error: ${e.message ?: "Unknown error"}"))
        }
    }




}

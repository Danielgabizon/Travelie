package org.colman.travelie.data.firebase

import org.colman.travelie.models.User
import org.colman.travelie.data.Error
import org.colman.travelie.data.Result
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import org.colman.travelie.models.AuthUser

data class AuthError(
    override val message: String
) : Error
data class UserError(
    override val message: String
) : Error



class RemoteFirebaseRepository : FirebaseRepository {
    private val auth = Firebase.auth
    private val firestore = Firebase.firestore
    private val usersCollection = firestore.collection("users")

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

    override suspend fun saveUser(user: User): Result<User, UserError> {
        return try {
            usersCollection.document(user.uid).set(user)
            Result.Success(user)
        } catch (e: Exception) {
            Result.Failure(UserError("Save user error: ${e.message ?: "Unknown error"}"))
        }
    }
    override suspend fun getUserDetails(uid: String): Result<User, UserError> {
        return try {
            val document = usersCollection.document(uid).get()

            if (document.exists) {
                val user = document.data<User>()
                Result.Success(user)
            } else {
                Result.Failure(UserError("User not found"))
            }
        } catch (e: Exception) {
            Result.Failure(UserError("Get user details error: ${e.message ?: "Unknown error"}"))
        }
    }

}
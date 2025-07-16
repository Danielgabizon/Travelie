package org.colman.travelie.data.firebase

import org.colman.travelie.models.User
import org.colman.travelie.data.Error
import org.colman.travelie.data.Result
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
//import dev.gitlive.firebase.firestore.firestore
data class AuthError(
    override val message: String
) : Error

class RemoteFirebaseRepository : FirebaseRepository {
    private val auth = Firebase.auth

    override suspend fun login(email: String, password: String): Result<User, AuthError> {
        return try {
            auth.signInWithEmailAndPassword(email, password)
            val currentUser = auth.currentUser

            if (currentUser != null) {
                Result.Success(
                    User(
                        uid = currentUser.uid,
                        email = currentUser.email ?: "",
                        displayName = currentUser.displayName ?: ""
                    )
                )
            } else {
                Result.Failure(AuthError("Login failed: user is null"))
            }
        } catch (e: Exception) {
            Result.Failure(AuthError("Login error: ${e.message ?: "Unknown error"}"))
        }

    }

    override suspend fun register(email: String, password: String): Result<User, AuthError> {
        return try {
            auth.createUserWithEmailAndPassword(email, password)
            val currentUser = auth.currentUser

            if (currentUser != null) {
                Result.Success(
                    User(
                        uid = currentUser.uid,
                        email = currentUser.email ?: "",
                        displayName = currentUser.displayName ?: ""
                    )
                )
            } else {
                Result.Failure(AuthError("Registration failed: user is null"))
            }
        } catch (e: Exception) {
            Result.Failure(AuthError("Registration error: ${e.message ?: "Unknown error"}"))
        }
    }
}
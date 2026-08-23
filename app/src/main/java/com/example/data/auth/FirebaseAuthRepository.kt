package com.example.data.auth

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

sealed class AuthResult<out T> {
    data class Success<out T>(val data: T) : AuthResult<T>()
    data class Error(val message: String, val exception: Exception? = null) : AuthResult<Nothing>()
}

class FirebaseAuthRepository(private val context: Context) {

    private val tag = "FirebaseAuthRepository"

    val isFirebaseInitialized: Boolean
        get() {
            return try {
                FirebaseApp.getApps(context).isNotEmpty()
            } catch (t: Throwable) {
                false
            }
        }

    val auth: FirebaseAuth?
        get() {
            return try {
                if (isFirebaseInitialized) FirebaseAuth.getInstance() else null
            } catch (t: Throwable) {
                Log.w(tag, "FirebaseAuth instance not available: ${t.message}")
                null
            }
        }

    val currentUser: FirebaseUser?
        get() = auth?.currentUser

    suspend fun signInWithEmail(email: String, password: String): AuthResult<FirebaseUser> {
        val authInstance = auth
        if (authInstance == null) {
            return AuthResult.Error("Firebase is not initialized. Please ensure google-services.json is added to the project.")
        }
        return try {
            val result = authInstance.signInWithEmailAndPassword(email.trim(), password).await()
            val user = result.user
            if (user != null) {
                AuthResult.Success(user)
            } else {
                AuthResult.Error("Authentication succeeded but no user record returned.")
            }
        } catch (e: Exception) {
            Log.e(tag, "Sign in failed", e)
            val friendlyMsg = when {
                e.message?.contains("password", ignoreCase = true) == true -> "Incorrect password. Please try again or reset your password."
                e.message?.contains("no user record", ignoreCase = true) == true || e.message?.contains("user-not-found", ignoreCase = true) == true -> "No account found with this email. Please register first."
                e.message?.contains("network", ignoreCase = true) == true -> "Network error. Please check your internet connection."
                e.message?.contains("invalid-email", ignoreCase = true) == true -> "Invalid email address format."
                else -> e.localizedMessage ?: "Sign in failed. Please check your credentials."
            }
            AuthResult.Error(friendlyMsg, e)
        }
    }

    suspend fun registerWithEmail(email: String, password: String): AuthResult<FirebaseUser> {
        val authInstance = auth
        if (authInstance == null) {
            return AuthResult.Error("Firebase is not initialized. Please ensure google-services.json is added to the project.")
        }
        return try {
            val result = authInstance.createUserWithEmailAndPassword(email.trim(), password).await()
            val user = result.user
            if (user != null) {
                AuthResult.Success(user)
            } else {
                AuthResult.Error("Registration succeeded but no user record was returned.")
            }
        } catch (e: Exception) {
            Log.e(tag, "Registration failed", e)
            val friendlyMsg = when {
                e.message?.contains("email-already-in-use", ignoreCase = true) == true -> "An account with this email already exists. Please sign in."
                e.message?.contains("weak-password", ignoreCase = true) == true -> "Password is too weak. Please use at least 6 characters."
                e.message?.contains("invalid-email", ignoreCase = true) == true -> "Invalid email address format."
                else -> e.localizedMessage ?: "Registration failed. Please try again."
            }
            AuthResult.Error(friendlyMsg, e)
        }
    }

    suspend fun sendPasswordReset(email: String): AuthResult<Unit> {
        val authInstance = auth
        if (authInstance == null) {
            return AuthResult.Error("Firebase is not initialized.")
        }
        return try {
            authInstance.sendPasswordResetEmail(email.trim()).await()
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Error(e.localizedMessage ?: "Failed to send password reset email.", e)
        }
    }

    fun signOut() {
        try {
            auth?.signOut()
        } catch (e: Exception) {
            Log.w(tag, "Error signing out: ${e.message}")
        }
    }
}

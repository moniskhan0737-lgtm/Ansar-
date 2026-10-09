package com.example.data

import com.example.model.UserAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Authentication management.
 * Modular architecture ready to be connected to Firebase Authentication.
 *
 * FIREBASE AUTH INTEGRATION STEPS:
 * 1. Add `implementation(libs.firebase.auth)` in app/build.gradle.kts
 * 2. In loginWithEmailAndPassword:
 *      FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
 *          .addOnSuccessListener { authResult -> ... }
 * 3. In signUpWithEmailAndPassword:
 *      FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
 *          .addOnSuccessListener { ... }
 * 4. In sendPasswordReset:
 *      FirebaseAuth.getInstance().sendPasswordResetEmail(email)
 */
class AuthRepository {

    // Default pre-loaded test student account so user can easily test the dashboard immediately
    private val defaultStudent = UserAccount(
        id = "user_demo_101",
        name = "Aman Verma",
        email = "aman.v@example.com",
        phone = "+91 98765 43210",
        isEnrolled = true,
        role = "STUDENT"
    )

    private val _currentUser = MutableStateFlow<UserAccount?>(defaultStudent)
    val currentUser: StateFlow<UserAccount?> = _currentUser.asStateFlow()

    val isStudentLoggedIn: Boolean
        get() = _currentUser.value != null && _currentUser.value?.role == "STUDENT"

    val isAdminLoggedIn: Boolean
        get() = _currentUser.value?.role == "ADMIN"

    fun login(email: String, name: String = "Student", role: String = "STUDENT"): Result<UserAccount> {
        val user = UserAccount(
            id = "user_${System.currentTimeMillis()}",
            name = if (role == "ADMIN") "Course Admin" else name,
            email = email,
            phone = "+91 98765 43210",
            isEnrolled = role != "ADMIN", // Students can view dashboard
            role = role
        )
        _currentUser.value = user
        return Result.success(user)
    }

    fun signup(name: String, email: String, phone: String): Result<UserAccount> {
        val user = UserAccount(
            id = "user_${System.currentTimeMillis()}",
            name = name,
            email = email,
            phone = phone,
            isEnrolled = false,
            role = "STUDENT"
        )
        _currentUser.value = user
        return Result.success(user)
    }

    fun markUserEnrolled() {
        val current = _currentUser.value
        if (current != null) {
            _currentUser.value = current.copy(isEnrolled = true)
        } else {
            _currentUser.value = UserAccount(
                id = "user_new_${System.currentTimeMillis()}",
                name = "Enrolled Student",
                email = "student@example.com",
                isEnrolled = true,
                role = "STUDENT"
            )
        }
    }

    fun logout() {
        _currentUser.value = null
    }

    companion object {
        val instance: AuthRepository by lazy { AuthRepository() }
    }
}

package org.colman.travelie.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.colman.travelie.models.User

class SessionManager {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> get() = _currentUser

    fun setUser(user: User?) {
        _currentUser.value = user
    }

    fun clearSession() {
        _currentUser.value = null
    }
}

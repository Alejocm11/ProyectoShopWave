package com.taller.proyectofinalcomponentes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.taller.proyectofinalcomponentes.domain.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AppUser(
    val email: String,
    val password: String,
    val role: UserRole
)

class AuthViewModel : ViewModel() {
    private val users = listOf(
        AppUser(email = "comprador@test.com", password = "1234", role = UserRole.Buyer),
        AppUser(email = "vendedor@test.com", password = "1234", role = UserRole.Seller)
    )

    private val _role = MutableStateFlow<UserRole?>(null)
    val role: StateFlow<UserRole?> = _role.asStateFlow()

    private val _currentEmail = MutableStateFlow("")
    val currentEmail: StateFlow<String> = _currentEmail.asStateFlow()

    fun login(email: String, password: String, selectedRole: UserRole): Boolean {
        val user = users.find {
            it.email.equals(email.trim(), ignoreCase = true) &&
                it.password == password &&
                it.role == selectedRole
        }

        return if (user != null) {
            _role.value = user.role
            _currentEmail.value = user.email
            true
        } else {
            _role.value = null
            _currentEmail.value = ""
            false
        }
    }

    fun logout() {
        _role.value = null
        _currentEmail.value = ""
    }
}

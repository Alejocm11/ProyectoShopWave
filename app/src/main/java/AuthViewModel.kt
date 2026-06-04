import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    fun login(email: String, password: String): Boolean {
        val found = FakeDatabase.users.find {
            it.email == email && it.password == password
        }
        _user.value = found
        return found != null
    }

    fun logout() {
        _user.value = null
    }
}

enum class UserRole {
    COMPRADOR,
    VENDEDOR
}

data class User(
    val id: Int,
    val email: String,
    val password: String,
    val role: UserRole
)

import com.taller.proyectofinalcomponentes.domain.model.Product

object FakeDatabase {
    val users = mutableListOf(
        User(id = 1, email = "comprador@test.com", password = "1234", role = UserRole.COMPRADOR),
        User(id = 2, email = "vendedor@test.com", password = "1234", role = UserRole.VENDEDOR)
    )

    val productos = mutableListOf<Product>()
    val compras = mutableListOf<Compra>()
    val favoritos = mutableListOf<Favorito>()
}

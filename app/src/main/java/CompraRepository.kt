object CompraRepository {

    fun realizarCompra(user: User, carrito: List<DetalleCompra>) {
        val compra = Compra(
            id = FakeDatabase.compras.size + 1,
            userId = user.id,
            productos = carrito
        )
        FakeDatabase.compras.add(compra)
    }

    fun obtenerComprasUsuario(userId: Int): List<Compra> {
        return FakeDatabase.compras.filter { it.userId == userId }
    }
}
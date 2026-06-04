package com.taller.proyectofinalcomponentes.domain.model

data class CartItem(
    val product: Product,
    val quantity: Int = 1
)

data class CartState(
    val items: List<CartItem> = emptyList()
) {
    val totalItems: Int get() = items.sumOf { it.quantity }
    val subtotal: Double get() = items.sumOf { it.product.price * it.quantity }
    val shipping: Double get() = if (items.isEmpty()) 0.0 else 12000.0
    val discount: Double get() = 0.0
    val total: Double get() = subtotal + shipping - discount
}

package com.taller.proyectofinalcomponentes.domain.model

data class OrderItemRecord(
    val product: Product,
    val quantity: Int,
    val subtotal: Double
)

data class OrderRecord(
    val id: Int,
    val buyerEmail: String,
    val items: List<OrderItemRecord>,
    val total: Double,
    val date: String
)

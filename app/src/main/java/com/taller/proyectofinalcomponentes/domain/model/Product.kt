package com.taller.proyectofinalcomponentes.domain.model

data class Product(
    val id: Int,
    val name: String,
    val category: String,
    val price: Double,
    val oldPrice: Double? = null,
    val rating: Double = 4.5,
    val description: String = "",
    val descriptionEs: String = "",
    val imageUrl: String = "",
    val sellerId: String = "default_seller"
)

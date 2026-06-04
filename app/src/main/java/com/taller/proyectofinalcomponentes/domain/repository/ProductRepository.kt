package com.taller.proyectofinalcomponentes.domain.repository

import com.taller.proyectofinalcomponentes.domain.model.Category
import com.taller.proyectofinalcomponentes.domain.model.Product

interface ProductRepository {
    fun getCategories(): List<Category>
    fun getProducts(): List<Product>
    fun getProductsByCategory(category: String): List<Product>
    fun getProductById(productId: Int): Product?
    fun searchProducts(query: String): List<Product>
    fun addProduct(product: Product)
}

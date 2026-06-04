package com.taller.proyectofinalcomponentes.data.repository

import com.taller.proyectofinalcomponentes.data.local.ShopWaveDataSource
import com.taller.proyectofinalcomponentes.domain.model.Category
import com.taller.proyectofinalcomponentes.domain.model.Product
import com.taller.proyectofinalcomponentes.domain.repository.ProductRepository

class ProductRepositoryImpl : ProductRepository {
    override fun getCategories(): List<Category> = ShopWaveDataSource.categories

    override fun getProducts(): List<Product> = ShopWaveDataSource.products

    override fun getProductsByCategory(category: String): List<Product> =
        ShopWaveDataSource.products.filter { it.category.equals(category, ignoreCase = true) }

    override fun getProductById(productId: Int): Product? =
        ShopWaveDataSource.products.find { it.id == productId }

    override fun searchProducts(query: String): List<Product> {
        if (query.isBlank()) return getProducts()
        return ShopWaveDataSource.products.filter {
            it.name.contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true) ||
                it.descriptionEs.contains(query, ignoreCase = true)
        }
    }

    override fun addProduct(product: Product) {
        ShopWaveDataSource.products.add(product)
    }
}

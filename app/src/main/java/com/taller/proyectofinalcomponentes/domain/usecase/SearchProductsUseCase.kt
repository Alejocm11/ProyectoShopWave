package com.taller.proyectofinalcomponentes.domain.usecase

import com.taller.proyectofinalcomponentes.domain.repository.ProductRepository

class SearchProductsUseCase(private val repository: ProductRepository) {
    operator fun invoke(query: String) = repository.searchProducts(query)
}

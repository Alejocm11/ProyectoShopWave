package com.taller.proyectofinalcomponentes.domain.usecase

import com.taller.proyectofinalcomponentes.domain.repository.ProductRepository

class GetProductsByCategoryUseCase(private val repository: ProductRepository) {
    operator fun invoke(category: String) = repository.getProductsByCategory(category)
}

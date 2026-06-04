package com.taller.proyectofinalcomponentes.domain.usecase

import com.taller.proyectofinalcomponentes.domain.repository.ProductRepository

class GetProductsUseCase(private val repository: ProductRepository) {
    operator fun invoke() = repository.getProducts()
}

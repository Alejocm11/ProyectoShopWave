package com.taller.proyectofinalcomponentes.domain.usecase

import com.taller.proyectofinalcomponentes.domain.repository.ProductRepository

class GetProductByIdUseCase(private val repository: ProductRepository) {
    operator fun invoke(productId: Int) = repository.getProductById(productId)
}

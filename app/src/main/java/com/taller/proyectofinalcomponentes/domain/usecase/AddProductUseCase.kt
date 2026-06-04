package com.taller.proyectofinalcomponentes.domain.usecase

import com.taller.proyectofinalcomponentes.domain.model.Product
import com.taller.proyectofinalcomponentes.domain.repository.ProductRepository

class AddProductUseCase(private val repository: ProductRepository) {
    operator fun invoke(product: Product) = repository.addProduct(product)
}

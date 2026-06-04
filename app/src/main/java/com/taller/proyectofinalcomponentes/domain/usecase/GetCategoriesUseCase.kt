package com.taller.proyectofinalcomponentes.domain.usecase

import com.taller.proyectofinalcomponentes.domain.repository.ProductRepository

class GetCategoriesUseCase(private val repository: ProductRepository) {
    operator fun invoke() = repository.getCategories()
}

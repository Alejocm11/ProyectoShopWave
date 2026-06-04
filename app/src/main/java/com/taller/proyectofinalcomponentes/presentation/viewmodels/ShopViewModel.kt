package com.taller.proyectofinalcomponentes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.taller.proyectofinalcomponentes.data.repository.ProductRepositoryImpl
import com.taller.proyectofinalcomponentes.domain.model.Category
import com.taller.proyectofinalcomponentes.domain.model.Product
import com.taller.proyectofinalcomponentes.domain.usecase.AddProductUseCase
import com.taller.proyectofinalcomponentes.domain.usecase.GetCategoriesUseCase
import com.taller.proyectofinalcomponentes.domain.usecase.GetProductByIdUseCase
import com.taller.proyectofinalcomponentes.domain.usecase.GetProductsByCategoryUseCase
import com.taller.proyectofinalcomponentes.domain.usecase.GetProductsUseCase
import com.taller.proyectofinalcomponentes.domain.usecase.SearchProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ShopUiState(
    val categories: List<Category> = emptyList(),
    val products: List<Product> = emptyList(),
    val searchQuery: String = "",
    val lastMessage: String? = null
)

class ShopViewModel : ViewModel() {
    private val repository = ProductRepositoryImpl()
    private val getProductsUseCase = GetProductsUseCase(repository)
    private val getCategoriesUseCase = GetCategoriesUseCase(repository)
    private val getProductsByCategoryUseCase = GetProductsByCategoryUseCase(repository)
    private val getProductByIdUseCase = GetProductByIdUseCase(repository)
    private val searchProductsUseCase = SearchProductsUseCase(repository)
    private val addProductUseCase = AddProductUseCase(repository)

    private val _uiState = MutableStateFlow(
        ShopUiState(
            categories = getCategoriesUseCase(),
            products = getProductsUseCase()
        )
    )
    val uiState: StateFlow<ShopUiState> = _uiState.asStateFlow()

    fun updateSearch(query: String) {
        _uiState.update {
            it.copy(
                searchQuery = query,
                products = searchProductsUseCase(query)
            )
        }
    }

    fun refreshProducts() {
        _uiState.update {
            it.copy(products = if (it.searchQuery.isBlank()) getProductsUseCase() else searchProductsUseCase(it.searchQuery))
        }
    }

    fun productsByCategory(category: String): List<Product> = getProductsByCategoryUseCase(category)

    fun productById(productId: Int): Product =
        getProductByIdUseCase(productId) ?: getProductsUseCase().first()

    fun addSellerProduct(
        name: String,
        description: String,
        category: String,
        priceText: String,
        oldPriceText: String
    ): Boolean {
        val price = priceText.replace(".", "").replace(",", ".").toDoubleOrNull()
        val oldPrice = oldPriceText.replace(".", "").replace(",", ".").toDoubleOrNull()

        if (name.isBlank() || description.isBlank() || category.isBlank() || price == null || price <= 0.0) {
            return false
        }

        val newProduct = Product(
            id = ((getProductsUseCase().maxOfOrNull { it.id } ?: 0) + 1),
            name = name.trim(),
            category = category,
            price = price,
            oldPrice = oldPrice,
            rating = 4.5,
            description = description.trim(),
            descriptionEs = description.trim(),
            sellerId = "seller_local"
        )

        addProductUseCase(newProduct)
        refreshProducts()
        _uiState.update { it.copy(lastMessage = "Producto creado") }
        return true
    }
}

package com.taller.proyectofinalcomponentes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.taller.proyectofinalcomponentes.domain.model.CartItem
import com.taller.proyectofinalcomponentes.domain.model.CartState
import com.taller.proyectofinalcomponentes.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CartViewModel : ViewModel() {
    private val _cartState = MutableStateFlow(CartState())
    val cartState: StateFlow<CartState> = _cartState.asStateFlow()

    fun addProduct(product: Product) {
        _cartState.update { state ->
            val existing = state.items.find { it.product.id == product.id }
            if (existing != null) {
                state.copy(
                    items = state.items.map {
                        if (it.product.id == product.id) it.copy(quantity = it.quantity + 1) else it
                    }
                )
            } else {
                state.copy(items = state.items + CartItem(product))
            }
        }
    }

    fun increaseQuantity(productId: Int) {
        _cartState.update { state ->
            state.copy(
                items = state.items.map {
                    if (it.product.id == productId) it.copy(quantity = it.quantity + 1) else it
                }
            )
        }
    }

    fun decreaseQuantity(productId: Int) {
        _cartState.update { state ->
            val updated = state.items.mapNotNull {
                if (it.product.id == productId) {
                    if (it.quantity > 1) it.copy(quantity = it.quantity - 1) else null
                } else it
            }
            state.copy(items = updated)
        }
    }

    fun removeItem(productId: Int) {
        _cartState.update { state ->
            state.copy(items = state.items.filter { it.product.id != productId })
        }
    }

    fun clearCart() {
        _cartState.value = CartState()
    }
}

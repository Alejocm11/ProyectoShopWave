package com.taller.proyectofinalcomponentes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.taller.proyectofinalcomponentes.domain.model.CartState
import com.taller.proyectofinalcomponentes.domain.model.OrderItemRecord
import com.taller.proyectofinalcomponentes.domain.model.OrderRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderHistoryViewModel : ViewModel() {

    private val _orders = MutableStateFlow<List<OrderRecord>>(emptyList())
    val orders: StateFlow<List<OrderRecord>> = _orders.asStateFlow()

    fun registerPurchase(buyerEmail: String, cartState: CartState) {
        if (cartState.items.isEmpty()) return

        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        val order = OrderRecord(
            id = (_orders.value.maxOfOrNull { it.id } ?: 0) + 1,
            buyerEmail = buyerEmail.ifBlank { "comprador@test.com" },
            items = cartState.items.map {
                OrderItemRecord(
                    product = it.product,
                    quantity = it.quantity,
                    subtotal = it.product.price * it.quantity
                )
            },
            total = cartState.total,
            date = formatter.format(Date())
        )

        _orders.value = _orders.value + order
    }

    fun purchasesByBuyer(email: String): List<OrderRecord> {
        return _orders.value.filter { it.buyerEmail.equals(email, ignoreCase = true) }
    }

    fun salesForSeller(): List<OrderItemRecord> {
        return _orders.value.flatMap { it.items }
    }

    fun totalSalesAmount(): Double {
        return salesForSeller().sumOf { it.subtotal }
    }
}

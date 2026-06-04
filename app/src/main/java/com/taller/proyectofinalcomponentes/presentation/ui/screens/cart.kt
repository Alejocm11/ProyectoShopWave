package com.taller.proyectofinalcomponentes.presentation.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.taller.proyectofinalcomponentes.domain.model.CartItem
import com.taller.proyectofinalcomponentes.core.localization.strings
import com.taller.proyectofinalcomponentes.core.localization.formatCop
import com.taller.proyectofinalcomponentes.presentation.viewmodels.CartViewModel
import com.taller.proyectofinalcomponentes.presentation.viewmodels.LanguageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    languageViewModel: LanguageViewModel,
    onBack: () -> Unit,
    onCheckout: () -> Unit
) {
    val cartState by cartViewModel.cartState.collectAsState()
    val language by languageViewModel.language.collectAsState()
    val text = strings(language)
    var couponCode by remember { mutableStateOf("") }
    var couponApplied by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text.cart, fontWeight = FontWeight.Bold)
                        if (cartState.totalItems > 0) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Badge(containerColor = Color(0xFF2563EB)) {
                                Text(cartState.totalItems.toString())
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = text.back)
                    }
                },
                actions = {
                    if (cartState.items.isNotEmpty()) {
                        TextButton(onClick = { cartViewModel.clearCart() }) {
                            Text(text.clear, color = Color(0xFFEF4444))
                        }
                    }
                }
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->

        if (cartState.items.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = Color(0xFFCBD5E1),
                        modifier = Modifier.size(96.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text.emptyCart, style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text.emptyCartDescription,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF94A3B8), textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onBack,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                    ) {
                        Text(text.continueShopping)
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cartState.items, key = { it.product.id }) { item ->
                        CartItemCard(
                            item = item,
                            onIncrease = { cartViewModel.increaseQuantity(item.product.id) },
                            onDecrease = { cartViewModel.decreaseQuantity(item.product.id) },
                            onRemove = { cartViewModel.removeItem(item.product.id) }
                        )
                    }

                    // Coupon input
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = couponCode,
                                    onValueChange = { couponCode = it.uppercase() },
                                    modifier = Modifier.weight(1f),
                                    placeholder = { Text("Coupon code") },
                                    leadingIcon = {
                                        Icon(Icons.Default.Discount, contentDescription = null,
                                            tint = Color(0xFF64748B))
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true
                                )
                                Button(
                                    onClick = { if (couponCode.isNotEmpty()) couponApplied = true },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (couponApplied) Color(0xFF22C55E)
                                                        else Color(0xFF2563EB)
                                    )
                                ) {
                                    Text(if (couponApplied) "Applied!" else "Apply")
                                }
                            }
                        }
                    }
                }

                // Order summary + CTA
                Surface(shadowElevation = 8.dp) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(text.orderSummary, style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            Spacer(modifier = Modifier.height(12.dp))
                            SummaryRow("Subtotal", formatCop(cartState.subtotal))
                            SummaryRow("Shipping", formatCop(cartState.shipping))
                            if (couponApplied) {
                                SummaryRow("Descuento (SAVE10)", "-${formatCop(20000.0)}", valueColor = Color(0xFF22C55E))
                            }
                            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                            SummaryRow(
                                "Total",
                                formatCop(cartState.total - if (couponApplied) 20000.0 else 0.0),
                                isBold = true
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = onCheckout,
                                modifier = Modifier.fillMaxWidth().height(55.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                            ) {
                                Icon(Icons.Default.ShoppingCartCheckout, contentDescription = null,
                                    modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text.checkout, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CartItemCard(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(Color(0xFFE2E8F0), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.ShoppingBag, contentDescription = null,
                    tint = Color(0xFF94A3B8), modifier = Modifier.size(32.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(item.product.name, style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A), maxLines = 2)
                Spacer(modifier = Modifier.height(4.dp))
                Text(formatCop(item.product.price), style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF2563EB), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledTonalIconButton(
                        onClick = onDecrease,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease",
                            modifier = Modifier.size(16.dp))
                    }
                    Text(item.quantity.toString(), fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium)
                    FilledTonalIconButton(
                        onClick = onIncrease,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase",
                            modifier = Modifier.size(16.dp))
                    }
                }
            }

            IconButton(onClick = onRemove) {
                Icon(Icons.Default.DeleteOutline, contentDescription = "Remove",
                    tint = Color(0xFFEF4444))
            }
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    valueColor: Color = Color(0xFF0F172A)
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium,
            color = if (isBold) Color(0xFF0F172A) else Color(0xFF64748B),
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal)
        Text(value, style = MaterialTheme.typography.bodyMedium,
            color = valueColor,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal)
    }
    Spacer(modifier = Modifier.height(6.dp))
}

package com.taller.proyectofinalcomponentes.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.taller.proyectofinalcomponentes.core.localization.AppLanguage
import com.taller.proyectofinalcomponentes.core.localization.categoryLabel
import com.taller.proyectofinalcomponentes.core.localization.formatCop
import com.taller.proyectofinalcomponentes.core.localization.strings
import com.taller.proyectofinalcomponentes.core.notifications.ShopWaveNotificationHelper
import com.taller.proyectofinalcomponentes.presentation.viewmodels.LanguageViewModel
import com.taller.proyectofinalcomponentes.presentation.viewmodels.ShopViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerProductScreen(
    shopViewModel: ShopViewModel,
    languageViewModel: LanguageViewModel,
    onSalesClick: () -> Unit,
    onLogout: () -> Unit
) {
    val shopState by shopViewModel.uiState.collectAsState()
    val language by languageViewModel.language.collectAsState()
    val text = strings(language)
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var oldPrice by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(shopState.categories.firstOrNull()?.name ?: "Electronics") }
    var message by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text.sellerPanel, fontWeight = FontWeight.Bold)
                        Text(text.sellerGreeting, style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
                    }
                },
                actions = {
                    TextButton(onClick = { languageViewModel.toggleLanguage() }) {
                        Text(if (language == AppLanguage.Spanish) "EN" else "ES")
                    }
                    TextButton(onClick = onSalesClick) {
                        Text(text.sales)
                    }
                    IconButton(onClick = {
                        ShopWaveNotificationHelper.showNotification(
                            context = context,
                            notificationId = 401,
                            title = text.productCreated,
                            message = text.sellerProducts
                        )
                    }) {
                        Icon(Icons.Default.Notifications, contentDescription = text.notifications)
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = text.logout)
                    }
                }
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Store, contentDescription = null, tint = Color(0xFF2563EB))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text.addProduct, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text(text.productName) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text(text.productDescription) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            shape = RoundedCornerShape(14.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(text.category, style = MaterialTheme.typography.labelMedium)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            shopState.categories.take(3).forEach { category ->
                                FilterChip(
                                    selected = selectedCategory == category.name,
                                    onClick = { selectedCategory = category.name },
                                    label = { Text(categoryLabel(category.name, language)) }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            shopState.categories.drop(3).forEach { category ->
                                FilterChip(
                                    selected = selectedCategory == category.name,
                                    onClick = { selectedCategory = category.name },
                                    label = { Text(categoryLabel(category.name, language)) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = price,
                            onValueChange = { price = it.filter { ch -> ch.isDigit() || ch == '.' || ch == ',' } },
                            label = { Text(text.priceCop) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = oldPrice,
                            onValueChange = { oldPrice = it.filter { ch -> ch.isDigit() || ch == '.' || ch == ',' } },
                            label = { Text(text.oldPriceCop) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                val created = shopViewModel.addSellerProduct(
                                    name = name,
                                    description = description,
                                    category = selectedCategory,
                                    priceText = price,
                                    oldPriceText = oldPrice
                                )

                                if (created) {
                                    name = ""
                                    description = ""
                                    price = ""
                                    oldPrice = ""
                                    message = text.productCreated
                                    ShopWaveNotificationHelper.showNotification(
                                        context = context,
                                        notificationId = 402,
                                        title = text.productCreated,
                                        message = text.addProduct
                                    )
                                } else {
                                    message = if (language == AppLanguage.Spanish) {
                                        "Completa nombre, descripción, categoría y precio válido"
                                    } else {
                                        "Complete name, description, category and valid price"
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(text.saveProduct)
                        }

                        message?.let {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(it, color = Color(0xFF2563EB), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            item {
                Text(text.sellerProducts, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            items(shopState.products) { product ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(product.name, fontWeight = FontWeight.Bold)
                        Text(categoryLabel(product.category, language), color = Color(0xFF64748B))
                        Text(formatCop(product.price), color = Color(0xFF2563EB), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

package com.taller.proyectofinalcomponentes.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.taller.proyectofinalcomponentes.core.localization.categoryLabel
import com.taller.proyectofinalcomponentes.core.localization.formatCop
import com.taller.proyectofinalcomponentes.core.localization.productDescription
import com.taller.proyectofinalcomponentes.core.localization.strings
import com.taller.proyectofinalcomponentes.core.notifications.ShopWaveNotificationHelper
import com.taller.proyectofinalcomponentes.domain.model.Product
import com.taller.proyectofinalcomponentes.presentation.viewmodels.CartViewModel
import com.taller.proyectofinalcomponentes.presentation.viewmodels.LanguageViewModel
import com.taller.proyectofinalcomponentes.presentation.viewmodels.ShopViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    category: String,
    cartViewModel: CartViewModel,
    shopViewModel: ShopViewModel,
    languageViewModel: LanguageViewModel,
    onBack: () -> Unit,
    onProductClick: (Int) -> Unit,
    onCartClick: () -> Unit
) {
    val cartState by cartViewModel.cartState.collectAsState()
    val language by languageViewModel.language.collectAsState()
    val text = strings(language)
    val context = LocalContext.current
    val products = shopViewModel.productsByCategory(category)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(categoryLabel(category, language), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = text.back)
                    }
                },
                actions = {
                    BadgedBox(badge = { if (cartState.totalItems > 0) Badge { Text(cartState.totalItems.toString()) } }) {
                        IconButton(onClick = onCartClick) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = text.cart)
                        }
                    }
                }
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.FilterList, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("${products.size} ${text.items}", color = Color(0xFF64748B), style = MaterialTheme.typography.bodySmall)
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products) { product ->
                    ProductGridCard(
                        product = product,
                        language = language,
                        onClick = { onProductClick(product.id) },
                        onAddToCart = {
                            cartViewModel.addProduct(product)
                            ShopWaveNotificationHelper.showNotification(
                                context = context,
                                notificationId = 100 + product.id,
                                title = text.cartNotificationTitle,
                                message = "${product.name}: ${text.cartNotificationBody}"
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductGridCard(
    product: Product,
    language: com.taller.proyectofinalcomponentes.core.localization.AppLanguage,
    onClick: (Int) -> Unit,
    onAddToCart: () -> Unit
) {
    val text = strings(language)
    var addedToCart by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick(product.id) },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier.fillMaxWidth().height(120.dp).background(Color(0xFFE2E8F0), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(categoryLabel(product.category, language), color = Color(0xFF475569), style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(product.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, maxLines = 2, color = Color(0xFF0F172A))
            Spacer(modifier = Modifier.height(4.dp))
            Text(productDescription(product, language), maxLines = 2, color = Color(0xFF64748B), style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(3.dp))
                Text(product.rating.toString(), style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(formatCop(product.price), color = Color(0xFF2563EB), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                    product.oldPrice?.let {
                        Text(formatCop(it), style = MaterialTheme.typography.bodySmall, color = Color(0xFF94A3B8))
                    }
                }
                IconButton(
                    onClick = {
                        onAddToCart()
                        addedToCart = true
                    },
                    modifier = Modifier.background(if (addedToCart) Color(0xFF22C55E) else Color(0xFF2563EB), RoundedCornerShape(12.dp)).size(38.dp)
                ) {
                    Icon(if (addedToCart) Icons.Default.Check else Icons.Default.Add, contentDescription = text.addToCart, tint = Color.White, modifier = Modifier.size(19.dp))
                }
            }
        }
    }
}

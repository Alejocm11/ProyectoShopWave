package com.taller.proyectofinalcomponentes.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.taller.proyectofinalcomponentes.core.localization.AppLanguage
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
fun HomeScreen(
    cartViewModel: CartViewModel,
    shopViewModel: ShopViewModel,
    languageViewModel: LanguageViewModel,
    onCategoryClick: (String) -> Unit,
    onCartClick: () -> Unit,
    onProductClick: (Int) -> Unit,
    onWishlistClick: () -> Unit,
    onProfileClick: () -> Unit,
    onPurchasesClick: () -> Unit,
    onLogout: () -> Unit
) {
    val cartState by cartViewModel.cartState.collectAsState()
    val shopState by shopViewModel.uiState.collectAsState()
    val language by languageViewModel.language.collectAsState()
    val text = strings(language)
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("ShopWave", fontWeight = FontWeight.Bold)
                        Text(text.homeGreeting, style = MaterialTheme.typography.bodySmall, color = Color(0xFF64748B))
                    }
                },
                actions = {
                    TextButton(onClick = { languageViewModel.toggleLanguage() }) {
                        Text(if (language == AppLanguage.Spanish) "EN" else "ES")
                    }
                    IconButton(onClick = {
                        ShopWaveNotificationHelper.showNotification(
                            context = context,
                            notificationId = 10,
                            title = text.promotionTitle,
                            message = text.promotionBody
                        )
                    }) {
                        Icon(Icons.Default.Notifications, contentDescription = text.notifications)
                    }
                    BadgedBox(badge = { if (cartState.totalItems > 0) Badge { Text(cartState.totalItems.toString()) } }) {
                        IconButton(onClick = onCartClick) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = text.cart)
                        }
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = text.logout)
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Default.Home, null) }, label = { Text(text.home) })
                NavigationBarItem(selected = false, onClick = onPurchasesClick, icon = { Icon(Icons.Default.ShoppingBag, null) }, label = { Text(text.purchases) })
                NavigationBarItem(selected = false, onClick = onCartClick, icon = { Icon(Icons.Default.ShoppingCart, null) }, label = { Text(text.cart) })
                NavigationBarItem(selected = false, onClick = onWishlistClick, icon = { Icon(Icons.Default.Favorite, null) }, label = { Text(text.wishlist) })
                NavigationBarItem(selected = false, onClick = onProfileClick, icon = { Icon(Icons.Default.Person, null) }, label = { Text(text.profile) })
            }
        },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = shopState.searchQuery,
                    onValueChange = { shopViewModel.updateSearch(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(text.searchProducts) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = text.search) },
                    trailingIcon = {
                        if (shopState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { shopViewModel.updateSearch("") }) {
                                Icon(Icons.Default.Close, contentDescription = text.clear)
                            }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(18.dp))
            }

            if (shopState.searchQuery.isBlank()) {
                item {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Brush.horizontalGradient(listOf(Color(0xFF7C3AED), Color(0xFF2563EB))))
                                .padding(20.dp)
                        ) {
                            Column {
                                Text(text.flashSale, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                Text(text.flashSaleDescription, color = Color.White.copy(alpha = 0.9f))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text.limitedTime, color = Color.White, style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(text.categories, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(shopState.categories) { category ->
                            FilterChip(
                                selected = false,
                                onClick = { onCategoryClick(category.name) },
                                label = { Text(categoryLabel(category.name, language)) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(text.featuredProducts, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        TextButton(onClick = { onCategoryClick("Electronics") }) { Text(text.seeAll) }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else {
                item {
                    Text("${shopState.products.size} ${text.results}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            items(shopState.products.take(if (shopState.searchQuery.isBlank()) 8 else shopState.products.size)) { product ->
                ProductListCard(
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
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun ProductListCard(
    product: Product,
    language: AppLanguage,
    onClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    val text = strings(language)
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(88.dp).background(Color(0xFFE2E8F0), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(categoryLabel(product.category, language), color = Color(0xFF475569), style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                Text(productDescription(product, language), maxLines = 2, color = Color(0xFF64748B), style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(6.dp))
                Text(formatCop(product.price), color = Color(0xFF2563EB), fontWeight = FontWeight.Bold)
            }
            IconButton(onClick = onAddToCart) {
                Icon(Icons.Default.AddShoppingCart, contentDescription = text.addToCart, tint = Color(0xFF2563EB))
            }
        }
    }
}

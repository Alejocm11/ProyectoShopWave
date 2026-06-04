package com.taller.proyectofinalcomponentes.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.taller.proyectofinalcomponentes.core.localization.categoryLabel
import com.taller.proyectofinalcomponentes.core.localization.formatCop
import com.taller.proyectofinalcomponentes.core.localization.productDescription
import com.taller.proyectofinalcomponentes.core.localization.strings
import com.taller.proyectofinalcomponentes.core.notifications.ShopWaveNotificationHelper
import com.taller.proyectofinalcomponentes.presentation.viewmodels.CartViewModel
import com.taller.proyectofinalcomponentes.presentation.viewmodels.LanguageViewModel
import com.taller.proyectofinalcomponentes.presentation.viewmodels.ShopViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Int,
    cartViewModel: CartViewModel,
    shopViewModel: ShopViewModel,
    languageViewModel: LanguageViewModel,
    onBack: () -> Unit,
    onGoToCart: () -> Unit
) {
    val cartState by cartViewModel.cartState.collectAsState()
    val language by languageViewModel.language.collectAsState()
    val text = strings(language)
    val context = LocalContext.current
    val product = shopViewModel.productById(productId)
    var quantity by remember { mutableStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text.productDetail) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = text.back)
                    }
                },
                actions = {
                    BadgedBox(badge = { if (cartState.totalItems > 0) Badge { Text(cartState.totalItems.toString()) } }) {
                        IconButton(onClick = onGoToCart) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = text.cart)
                        }
                    }
                }
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(
                modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp)
            ) {
                val gradient = Brush.linearGradient(colors = listOf(Color(0xFFE2E8F0), Color(0xFFCBD5E1)))
                Box(
                    modifier = Modifier.fillMaxWidth().height(260.dp).background(gradient, RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(categoryLabel(product.category, language), color = Color(0xFF64748B), style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                    Text(product.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A), modifier = Modifier.weight(1f))
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = text.wishlist, tint = Color(0xFF94A3B8))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)), shape = RoundedCornerShape(8.dp)) {
                        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${product.rating}", fontWeight = FontWeight.SemiBold, color = Color(0xFF92400E))
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("• ${categoryLabel(product.category, language)}", color = Color(0xFF64748B), style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(formatCop(product.price), style = MaterialTheme.typography.headlineSmall, color = Color(0xFF2563EB), fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(12.dp))
                    product.oldPrice?.let { old ->
                        Text(formatCop(old), color = Color(0xFF94A3B8), style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.width(8.dp))
                        val disc = ((old - product.price) / old * 100).toInt()
                        Badge(containerColor = Color(0xFF22C55E)) {
                            Text("-$disc%", color = Color.White, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(text.description, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                Spacer(modifier = Modifier.height(8.dp))
                Text(productDescription(product, language), style = MaterialTheme.typography.bodyMedium, color = Color(0xFF475569))

                Spacer(modifier = Modifier.height(24.dp))

                Text(text.quantity, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { if (quantity > 1) quantity-- }) {
                        Icon(Icons.Default.Remove, contentDescription = null)
                    }
                    Text(quantity.toString(), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 20.dp))
                    IconButton(onClick = { quantity++ }) {
                        Icon(Icons.Default.Add, contentDescription = null)
                    }
                }
            }

            Surface(shadowElevation = 10.dp) {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(formatCop(product.price * quantity), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB), modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            repeat(quantity) { cartViewModel.addProduct(product) }
                            ShopWaveNotificationHelper.showNotification(
                                context = context,
                                notificationId = 200 + product.id,
                                title = text.cartNotificationTitle,
                                message = "${product.name}: ${text.cartNotificationBody}"
                            )
                        },
                        modifier = Modifier.weight(1f).height(54.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(text.addToCart)
                    }
                }
            }
        }
    }
}

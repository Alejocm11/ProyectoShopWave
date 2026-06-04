package com.taller.proyectofinalcomponentes.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.taller.proyectofinalcomponentes.core.localization.categoryLabel
import com.taller.proyectofinalcomponentes.core.localization.formatCop
import com.taller.proyectofinalcomponentes.core.localization.strings
import com.taller.proyectofinalcomponentes.presentation.viewmodels.LanguageViewModel
import com.taller.proyectofinalcomponentes.presentation.viewmodels.ShopViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(
    shopViewModel: ShopViewModel,
    languageViewModel: LanguageViewModel,
    onBack: () -> Unit
) {
    val shopState by shopViewModel.uiState.collectAsState()
    val language by languageViewModel.language.collectAsState()
    val text = strings(language)

    val favoriteProducts = shopState.products.take(4)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text.wishlist, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = text.back)
                    }
                }
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = if (language.name == "Spanish") "Productos guardados como favoritos" else "Saved favorite products",
                    color = Color(0xFF64748B)
                )
            }

            items(favoriteProducts) { product ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(70.dp).background(Color(0xFFE2E8F0), RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = Color(0xFF64748B))
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(product.name, fontWeight = FontWeight.Bold)
                            Text(categoryLabel(product.category, language), color = Color(0xFF64748B))
                            Text(formatCop(product.price), color = Color(0xFF2563EB), fontWeight = FontWeight.Bold)
                        }

                        Icon(Icons.Default.Favorite, contentDescription = text.wishlist, tint = Color(0xFFEF4444))
                    }
                }
            }
        }
    }
}

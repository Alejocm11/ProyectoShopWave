package com.taller.proyectofinalcomponentes.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.taller.proyectofinalcomponentes.core.localization.formatCop
import com.taller.proyectofinalcomponentes.core.localization.strings
import com.taller.proyectofinalcomponentes.presentation.viewmodels.LanguageViewModel
import com.taller.proyectofinalcomponentes.presentation.viewmodels.OrderHistoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesHistoryScreen(
    languageViewModel: LanguageViewModel,
    orderHistoryViewModel: OrderHistoryViewModel,
    onBack: () -> Unit
) {
    val language by languageViewModel.language.collectAsState()
    val text = strings(language)
    val orders by orderHistoryViewModel.orders.collectAsState()
    val sales = orderHistoryViewModel.salesForSeller()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text.salesHistory, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = text.back)
                    }
                }
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->
        if (orders.isEmpty() || sales.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp)) {
                Text(text.noSales, color = Color(0xFF64748B))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.Assessment, contentDescription = null, tint = Color(0xFF2563EB))
                                Text(text.totalSold, fontWeight = FontWeight.Bold)
                            }
                            Text(formatCop(orderHistoryViewModel.totalSalesAmount()), color = Color(0xFF2563EB), fontWeight = FontWeight.Bold)
                        }
                    }
                }

                items(sales) { sale ->
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(sale.product.name, fontWeight = FontWeight.Bold)
                            Text("Cantidad: ${sale.quantity}", color = Color(0xFF64748B))
                            Text(formatCop(sale.subtotal), color = Color(0xFF2563EB), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

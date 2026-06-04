package com.taller.proyectofinalcomponentes.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.taller.proyectofinalcomponentes.core.localization.strings
import com.taller.proyectofinalcomponentes.core.localization.formatCop
import com.taller.proyectofinalcomponentes.core.notifications.ShopWaveNotificationHelper
import com.taller.proyectofinalcomponentes.presentation.viewmodels.CartViewModel
import com.taller.proyectofinalcomponentes.presentation.viewmodels.AuthViewModel
import com.taller.proyectofinalcomponentes.presentation.viewmodels.OrderHistoryViewModel
import com.taller.proyectofinalcomponentes.presentation.viewmodels.LanguageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    cartViewModel: CartViewModel,
    languageViewModel: LanguageViewModel,
    authViewModel: AuthViewModel,
    orderHistoryViewModel: OrderHistoryViewModel,
    onBack: () -> Unit,
    onFinish: () -> Unit
) {
    val cartState by cartViewModel.cartState.collectAsState()
    val language by languageViewModel.language.collectAsState()
    val text = strings(language)
    val context = LocalContext.current
    val buyerEmail by authViewModel.currentEmail.collectAsState()
    var currentStep by remember { mutableStateOf(0) }
    var address by remember { mutableStateOf("Calle 123 #45-67, Bogotá") }
    var selectedPayment by remember { mutableStateOf("card") }
    var cardNumber by remember { mutableStateOf("") }
    var cardName by remember { mutableStateOf("") }
    var cardExpiry by remember { mutableStateOf("") }
    var orderPlaced by remember { mutableStateOf(false) }

    val steps = listOf(text.address, text.paymentMethod, text.reviewOrder, text.placeOrder)

    if (orderPlaced) {
        // Success screen
        Box(
            modifier = Modifier.fillMaxSize().background(Color(0xFFF8FAFC)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color(0xFF22C55E), RoundedCornerShape(50.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Check, contentDescription = null,
                        tint = Color.White, modifier = Modifier.size(52.dp))
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(text.orderPlaced, style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                Spacer(modifier = Modifier.height(8.dp))
                Text(text.orderConfirmed,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF64748B), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Order #SW${(10000..99999).random()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF2563EB), fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        cartViewModel.clearCart()
                        onFinish()
                    },
                    modifier = Modifier.fillMaxWidth().height(55.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                ) {
                    Text(text.continueShopping, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text.checkout, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentStep > 0) currentStep-- else onBack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = text.back)
                    }
                }
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Step indicator
            StepIndicator(steps = steps, currentStep = currentStep)

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                when (currentStep) {
                    0 -> AddressStep(address = address, onAddressChange = { address = it })
                    1 -> PaymentStep(
                        selected = selectedPayment,
                        onSelectPayment = { selectedPayment = it },
                        cardNumber = cardNumber, onCardNumberChange = { cardNumber = it },
                        cardName = cardName, onCardNameChange = { cardName = it },
                        cardExpiry = cardExpiry, onCardExpiryChange = { cardExpiry = it }
                    )
                    2 -> ReviewStep(cartState = cartState, address = address, payment = selectedPayment)
                    3 -> ConfirmStep(cartState = cartState)
                }
            }

            // Bottom navigation
            Surface(shadowElevation = 8.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (currentStep > 0) {
                        OutlinedButton(
                            onClick = { currentStep-- },
                            modifier = Modifier.height(55.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(text.back)
                        }
                    }
                    Button(
                        onClick = {
                            if (currentStep < steps.size - 1) {
                                currentStep++
                            } else {
                                orderHistoryViewModel.registerPurchase(
                                    buyerEmail = buyerEmail,
                                    cartState = cartState
                                )
                                ShopWaveNotificationHelper.showNotification(
                                    context = context,
                                    notificationId = 300,
                                    title = text.orderNotificationTitle,
                                    message = text.orderNotificationBody
                                )
                                orderPlaced = true
                            }
                        },
                        modifier = Modifier.weight(1f).height(55.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (currentStep == steps.size - 1)
                                Color(0xFF22C55E) else Color(0xFF2563EB)
                        )
                    ) {
                        Text(
                            when (currentStep) {
                                steps.size - 1 -> text.placeOrder
                                steps.size - 2 -> text.reviewOrder
                                else -> text.continueText
                            },
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StepIndicator(steps: List<String>, currentStep: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, label ->
            val isActive = index == currentStep
            val isDone = index < currentStep

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            when {
                                isDone -> Color(0xFF22C55E)
                                isActive -> Color(0xFF2563EB)
                                else -> Color(0xFFE2E8F0)
                            },
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isDone) {
                        Icon(Icons.Default.Check, contentDescription = null,
                            tint = Color.White, modifier = Modifier.size(16.dp))
                    } else {
                        Text(
                            "${index + 1}",
                            color = if (isActive) Color.White else Color(0xFF94A3B8),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(label, style = MaterialTheme.typography.labelSmall,
                    color = if (isActive) Color(0xFF2563EB) else Color(0xFF94A3B8),
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal)
            }

            if (index < steps.size - 1) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(2.dp)
                        .padding(horizontal = 4.dp)
                        .background(
                            if (index < currentStep) Color(0xFF22C55E) else Color(0xFFE2E8F0),
                            RoundedCornerShape(1.dp)
                        )
                )
            }
        }
    }
}

@Composable
private fun AddressStep(address: String, onAddressChange: (String) -> Unit) {
    Column {
        Text("Delivery Address", style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = address,
                    onValueChange = onAddressChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Street address") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = "Bogotá",
                        onValueChange = {},
                        modifier = Modifier.weight(1f),
                        label = { Text("City") },
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = "110111",
                        onValueChange = {},
                        modifier = Modifier.weight(1f),
                        label = { Text("ZIP") },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentStep(
    selected: String, onSelectPayment: (String) -> Unit,
    cardNumber: String, onCardNumberChange: (String) -> Unit,
    cardName: String, onCardNameChange: (String) -> Unit,
    cardExpiry: String, onCardExpiryChange: (String) -> Unit
) {
    Column {
        Text("Payment Method", style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
        Spacer(modifier = Modifier.height(16.dp))

        val methods = listOf(
            Triple("card", "Credit / Debit Card", Icons.Default.CreditCard),
            Triple("gpay", "Google Pay", Icons.Default.Payments),
            Triple("paypal", "PayPal", Icons.Default.AccountBalance),
            Triple("wallet", "Wallet Balance", Icons.Default.AccountBalanceWallet)
        )

        methods.forEach { (id, label, icon) ->
            val isSelected = selected == id
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onSelectPayment(id) }
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) Color(0xFF2563EB) else Color(0xFFE2E8F0),
                        shape = RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Color(0xFFEFF6FF) else Color.White
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { onSelectPayment(id) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(icon, contentDescription = null,
                        tint = if (isSelected) Color(0xFF2563EB) else Color(0xFF64748B))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(label, fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal)
                }
            }
        }

        if (selected == "card") {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = cardNumber, onValueChange = onCardNumberChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Card number") },
                        placeholder = { Text("1234 5678 9012 3456") },
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = cardName, onValueChange = onCardNameChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Cardholder name") },
                        shape = RoundedCornerShape(12.dp)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = cardExpiry, onValueChange = onCardExpiryChange,
                            modifier = Modifier.weight(1f),
                            label = { Text("MM/YY") },
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = "", onValueChange = {},
                            modifier = Modifier.weight(1f),
                            label = { Text("CVV") },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewStep(cartState: com.taller.proyectofinalcomponentes.domain.model.CartState,
                       address: String, payment: String) {
    Column {
        Text("Review Order", style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
        Spacer(modifier = Modifier.height(16.dp))
        Card(shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Delivery to", style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF64748B))
                Text(address, fontWeight = FontWeight.SemiBold)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                Text("Items (${cartState.totalItems})", style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF64748B))
                cartState.items.forEach { item ->
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${item.product.name} ×${item.quantity}",
                            modifier = Modifier.weight(1f), maxLines = 1)
                        Text(formatCop(item.product.price * item.quantity),
                            fontWeight = FontWeight.SemiBold, color = Color(0xFF2563EB))
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total", fontWeight = FontWeight.Bold)
                    Text(formatCop(cartState.total),
                        fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
                }
            }
        }
    }
}

@Composable
private fun ConfirmStep(cartState: com.taller.proyectofinalcomponentes.domain.model.CartState) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(16.dp))
        Icon(Icons.Default.Lock, contentDescription = null,
            tint = Color(0xFF22C55E), modifier = Modifier.size(48.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Text("Secure Checkout", style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
        Spacer(modifier = Modifier.height(8.dp))
        Text("Your payment is protected with SSL encryption.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF64748B), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(20.dp))
        Card(shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF))) {
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text("Order Total", fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                Text(formatCop(cartState.total),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
            }
        }
    }
}

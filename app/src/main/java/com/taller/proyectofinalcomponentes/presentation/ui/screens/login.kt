package com.taller.proyectofinalcomponentes.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.taller.proyectofinalcomponentes.core.localization.AppLanguage
import com.taller.proyectofinalcomponentes.core.localization.strings
import com.taller.proyectofinalcomponentes.domain.model.UserRole
import com.taller.proyectofinalcomponentes.presentation.viewmodels.LanguageViewModel
import com.taller.proyectofinalcomponentes.presentation.viewmodels.AuthViewModel

@Composable
fun LoginScreen(
    languageViewModel: LanguageViewModel,
    authViewModel: AuthViewModel,
    onLoginSuccess: (UserRole) -> Unit
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(UserRole.Buyer) }
    var loginError by remember { mutableStateOf<String?>(null) }
    val language by languageViewModel.language.collectAsState()
    val text = strings(language)

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF0F172A), Color(0xFF1E3A8A), Color(0xFF3B82F6))
    )

    Scaffold { padding ->
        Box(
            modifier = Modifier.fillMaxSize().background(gradient).padding(padding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.clip(CircleShape).background(Color.White.copy(alpha = 0.18f)).padding(18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Logo", tint = Color.White)
                }

                Spacer(modifier = Modifier.height(18.dp))
                Text("ShopWave", style = MaterialTheme.typography.headlineMedium, color = Color.White)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text.appSubtitle, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.85f))
                Spacer(modifier = Modifier.height(28.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(22.dp)) {
                        Text(text.loginTitle, style = MaterialTheme.typography.titleLarge, color = Color(0xFF0F172A))
                        Text(text.loginSubtitle, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF64748B))
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text.selectAccountType, style = MaterialTheme.typography.labelMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(
                                selected = selectedRole == UserRole.Buyer,
                                onClick = { selectedRole = UserRole.Buyer },
                                label = { Text(text.buyer) }
                            )
                            FilterChip(
                                selected = selectedRole == UserRole.Seller,
                                onClick = { selectedRole = UserRole.Seller },
                                label = { Text(text.seller) }
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        OutlinedTextField(
                            value = email.value,
                            onValueChange = { email.value = it },
                            label = { Text(text.email) },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = password.value,
                            onValueChange = { password.value = it },
                            label = { Text(text.password) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        )

                        loginError?.let {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = it,
                                color = Color(0xFFDC2626),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (language == AppLanguage.Spanish)
                                "Credenciales: comprador@test.com / 1234 o vendedor@test.com / 1234"
                            else
                                "Credentials: comprador@test.com / 1234 or vendedor@test.com / 1234",
                            color = Color(0xFF64748B),
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(modifier = Modifier.height(18.dp))
                        Button(
                            onClick = {
                                val ok = authViewModel.login(email.value, password.value, selectedRole)
                                if (ok) {
                                    loginError = null
                                    onLoginSuccess(selectedRole)
                                } else {
                                    loginError = if (language == AppLanguage.Spanish)
                                        "Correo, contraseña o tipo de cuenta incorrectos"
                                    else
                                        "Wrong email, password or account type"
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(54.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                        ) {
                            Text("${text.loginButton} - ${if (selectedRole == UserRole.Seller) text.seller else text.buyer}")
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedButton(
                            onClick = { languageViewModel.toggleLanguage() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(if (language == AppLanguage.Spanish) text.english else text.spanish)
                        }
                    }
                }
            }
        }
    }
}

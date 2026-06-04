package com.taller.proyectofinalcomponentes.core.localization

import com.taller.proyectofinalcomponentes.domain.model.Product
import java.text.NumberFormat
import java.util.Locale

fun formatCop(value: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
    formatter.maximumFractionDigits = 0
    return formatter.format(value)
}

fun categoryLabel(category: String, language: AppLanguage): String {
    val text = strings(language)
    return when (category) {
        "Electronics" -> text.electronics
        "Fashion" -> text.fashion
        "Home" -> text.homeCategory
        "Beauty" -> text.beauty
        "Sports" -> text.sports
        "Food" -> text.food
        else -> category
    }
}

fun productDescription(product: Product, language: AppLanguage): String {
    return if (language == AppLanguage.Spanish && product.descriptionEs.isNotBlank()) {
        product.descriptionEs
    } else {
        product.description
    }
}

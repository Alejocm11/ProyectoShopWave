package com.taller.proyectofinalcomponentes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.taller.proyectofinalcomponentes.core.localization.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LanguageViewModel : ViewModel() {
    private val _language = MutableStateFlow(AppLanguage.Spanish)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    fun setLanguage(language: AppLanguage) {
        _language.value = language
    }

    fun toggleLanguage() {
        _language.value = if (_language.value == AppLanguage.Spanish) {
            AppLanguage.English
        } else {
            AppLanguage.Spanish
        }
    }
}

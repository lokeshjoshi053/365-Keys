package com.example.housingroperty.ui.components

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(
    enabled: Boolean,
    onBack: () -> Unit
) {
    // iOS handles back through top bar UI buttons and native gestures
}

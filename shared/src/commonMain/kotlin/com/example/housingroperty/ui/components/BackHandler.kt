package com.example.housingroperty.ui.components

import androidx.compose.runtime.Composable

/**
 * Multiplatform BackHandler that intercepts hardware/gesture back events on Android
 * and safely no-ops on iOS.
 */
@Composable
expect fun BackHandler(
    enabled: Boolean = true,
    onBack: () -> Unit
)

package com.example.housingroperty.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = BrandBluePrimary,
    onPrimary = BackgroundWhite,
    primaryContainer = BrandBlueSoft,
    onPrimaryContainer = BrandBlueDark,
    background = BackgroundWhite,
    surface = BackgroundWhite,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceLight,
    onSurfaceVariant = TextSecondary,
    outline = BorderLight
)

@Composable
fun PlotsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}

package com.example.housingroperty

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.housingroperty.ui.navigation.OnboardingFlow
import com.example.housingroperty.ui.theme.PlotsTheme

@Composable
fun App() {
    PlotsTheme {
        OnboardingFlow()
    }
}
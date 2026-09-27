package com.example.housingroperty.ui.customer.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.ui.components.PrimaryButton
import com.example.housingroperty.ui.theme.*

@Composable
fun ProfileSetupScreen(
    onSaveSuccess: (name: String, email: String) -> Unit,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    initialName: String = "Rohan Mehta",
    initialEmail: String = "rohan@email.com"
) {
    var fullName by remember { mutableStateOf(initialName) }
    var email by remember { mutableStateOf(initialEmail) }
    var nomineeName by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
            .imePadding()
    ) {
        // Header
        com.example.housingroperty.ui.components.AppTopBar(
            title = "Set up profile",
            subtitle = "Personal & nominee details",
            onBackClick = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Full Name Input
            InputFieldSection(
                label = "Full Name",
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = "Enter your full name"
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Email Input
            InputFieldSection(
                label = "Email",
                value = email,
                onValueChange = { email = it },
                placeholder = "name@email.com",
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Nominee Name (optional)
            InputFieldSection(
                label = "Nominee Name (optional)",
                value = nomineeName,
                onValueChange = { nomineeName = it },
                placeholder = "Add nominee"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Notice Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFF8FAFC),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Text(
                    text = "Your details are used only for booking & document generation.",
                    fontSize = 13.sp,
                    color = TextMuted,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Save & Continue Button
            PrimaryButton(
                text = "Save & Continue",
                onClick = {
                    isSaving = true
                    onSaveSuccess(fullName, email)
                },
                enabled = fullName.isNotBlank() && email.isNotBlank(),
                isLoading = isSaving,
                modifier = Modifier.padding(bottom = 24.dp, top = 20.dp)
            )
        }
    }
}

@Composable
private fun InputFieldSection(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextSecondary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
                .background(Color(0xFFFAFAFA))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                cursorBrush = SolidColor(BrandBluePrimary),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 15.sp,
                            color = TextSubtle,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

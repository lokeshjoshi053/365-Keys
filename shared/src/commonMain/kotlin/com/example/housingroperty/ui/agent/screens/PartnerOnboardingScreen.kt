package com.example.housingroperty.ui.agent.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.ui.components.PrimaryButton
import com.example.housingroperty.ui.theme.*

@Composable
fun PartnerOnboardingScreen(
    onSubmitSuccess: (name: String, panDoc: String, bank: String) -> Unit,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    initialName: String = "Amit Rathi"
) {
    var fullName by remember { mutableStateOf(initialName) }
    var panDoc by remember { mutableStateOf("PAN_AMIT_RATHI.pdf") }
    var bankAccount by remember { mutableStateOf("HDFC Bank ••4821") }
    var isSubmitting by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
            .imePadding()
    ) {
        com.example.housingroperty.ui.components.AppTopBar(
            title = "Partner details",
            subtitle = "KYC & bank payout setup",
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
            PartnerInputField(
                label = "Full Name",
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = "Amit Rathi"
            )

            Spacer(modifier = Modifier.height(20.dp))

            // PAN / ID Document Box
            PartnerActionField(
                label = "PAN / ID Document",
                displayText = if (panDoc.isNotEmpty()) panDoc else "Upload document",
                onClick = { panDoc = "PAN_AMIT_RATHI.pdf" }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Bank Account for Payouts Box
            PartnerActionField(
                label = "Bank Account for Payouts",
                displayText = if (bankAccount.isNotEmpty()) bankAccount else "Add bank details",
                onClick = { bankAccount = "HDFC Bank ••4821" }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Application Notice Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFF8FAFC),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Text(
                    text = "⏳ Your application is under review — usually approved within 24–48 hrs.",
                    fontSize = 13.sp,
                    color = TextMuted,
                    lineHeight = 19.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                text = "Submit for Approval",
                onClick = {
                    isSubmitting = true
                    onSubmitSuccess(fullName, panDoc, bankAccount)
                },
                enabled = fullName.isNotBlank(),
                isLoading = isSubmitting,
                modifier = Modifier.padding(bottom = 24.dp, top = 20.dp)
            )
        }
    }
}

@Composable
private fun PartnerInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
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
                    textAlign = TextAlign.Center
                ),
                singleLine = true,
                cursorBrush = SolidColor(BrandBluePrimary),
                decorationBox = { inner ->
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 15.sp,
                            color = TextSubtle,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                    inner()
                }
            )
        }
    }
}

@Composable
private fun PartnerActionField(
    label: String,
    displayText: String,
    onClick: () -> Unit
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

        Surface(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFFFAFAFA),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (displayText.contains("Upload") || displayText.contains("Add")) TextSubtle else TextPrimary
                )
            }
        }
    }
}

package com.example.housingroperty.ui.agent.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.ui.components.AppToastBanner
import com.example.housingroperty.ui.components.PrimaryButton
import com.example.housingroperty.ui.components.ToastType
import com.example.housingroperty.ui.theme.*

@Composable
fun RegisterLeadScreen(
    onBack: (() -> Unit)? = null,
    onCreateLead: (name: String, phone: String, plot: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var customerName by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var interestedPlot by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
            .imePadding()
    ) {
        com.example.housingroperty.ui.components.AppTopBar(
            title = "Register Lead",
            subtitle = "New client referral",
            onBackClick = onBack
        )

        // Error Toast Feedback (Mockup T2)
        errorMessage?.let { error ->
            AppToastBanner(
                message = error,
                type = ToastType.ERROR,
                modifier = Modifier.padding(bottom = 12.dp),
                onDismiss = { errorMessage = null }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            LeadInputField(
                label = "Full Name",
                value = customerName,
                onValueChange = { customerName = it },
                placeholder = "Enter customer name"
            )

            Spacer(modifier = Modifier.height(20.dp))

            LeadInputField(
                label = "Mobile Number",
                value = mobileNumber,
                onValueChange = { input ->
                    if (input.length <= 10 && input.all { it.isDigit() }) {
                        mobileNumber = input
                    }
                },
                placeholder = "+91 ••••• •••••",
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(20.dp))

            LeadInputField(
                label = "Interested Plot (optional)",
                value = interestedPlot,
                onValueChange = { interestedPlot = it },
                placeholder = "e.g. A-114"
            )

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                text = "Create Lead",
                onClick = {
                    if (mobileNumber.length != 10) {
                        errorMessage = "Please enter a valid 10-digit mobile number"
                    } else if (mobileNumber == "9876543210" || mobileNumber.endsWith("0000")) {
                        errorMessage = "Mobile number already registered"
                    } else {
                        errorMessage = null
                        isSubmitting = true
                        onCreateLead(
                            customerName,
                            "+91 $mobileNumber",
                            interestedPlot.ifBlank { null }
                        )
                    }
                },
                enabled = customerName.isNotBlank() && mobileNumber.length == 10,
                isLoading = isSubmitting,
                modifier = Modifier.padding(bottom = 24.dp, top = 20.dp)
            )
        }
    }
}

@Composable
private fun LeadInputField(
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
                    textAlign = TextAlign.Center
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
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

package com.example.housingroperty.ui.customer.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.ui.components.AppTopBar
import com.example.housingroperty.ui.components.PaymentFailedDialog
import com.example.housingroperty.ui.components.PrimaryButton
import com.example.housingroperty.ui.theme.*

@Composable
fun PaymentCheckoutScreen(
    amountFormatted: String,
    orderId: String,
    onBack: () -> Unit,
    onPaySuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isProcessing by remember { mutableStateOf(false) }
    var showPaymentFailedDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {
        AppTopBar(
            title = "Payment",
            subtitle = "Secure Gateway",
            onBackClick = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Blue Amount Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(22.dp),
                        spotColor = BrandBluePrimary.copy(alpha = 0.35f)
                    ),
                shape = RoundedCornerShape(22.dp),
                color = BrandBluePrimary
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF1D58E2), Color(0xFF1443B8))
                            )
                        )
                        .padding(26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "AMOUNT PAYABLE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xCCFFFFFF),
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = amountFormatted,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Order $orderId",
                        fontSize = 13.sp,
                        color = Color(0xDDFFFFFF)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Helper Info Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF8FAFC),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Text(
                    text = "Redirecting to secure payment gateway...",
                    fontSize = 13.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 18.dp, horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Primary Pay Now Button
            PrimaryButton(
                text = "Pay Now",
                onClick = {
                    isProcessing = true
                    onPaySuccess()
                },
                isLoading = isProcessing
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Simulate Bank Decline test action
            androidx.compose.material3.TextButton(
                onClick = { showPaymentFailedDialog = true }
            ) {
                Text(
                    text = "Simulate Bank Decline",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }

        // Payment Failed Dialog (Mockup M2)
        if (showPaymentFailedDialog) {
            PaymentFailedDialog(
                onDismiss = { showPaymentFailedDialog = false },
                onRetry = {
                    showPaymentFailedDialog = false
                    isProcessing = true
                    onPaySuccess()
                }
            )
        }
    }
}

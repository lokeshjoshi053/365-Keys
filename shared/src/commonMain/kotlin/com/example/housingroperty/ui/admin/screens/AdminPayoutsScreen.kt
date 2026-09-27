package com.example.housingroperty.ui.admin.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.data.models.AdminPayoutRequest
import com.example.housingroperty.ui.agent.components.BankIcon
import com.example.housingroperty.ui.components.AppToastBanner
import com.example.housingroperty.ui.components.AppTopBar
import com.example.housingroperty.ui.components.ToastType
import com.example.housingroperty.ui.theme.*

@Composable
fun AdminPayoutsScreen(
    payouts: List<AdminPayoutRequest>,
    onProcessPayout: (String) -> Unit,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val totalQueueAmountFormatted = remember(payouts) {
        val total = payouts.filter { it.status == "Pending" }.sumOf {
            it.amount.replace("₹", "").replace(",", "").trim().toLongOrNull() ?: 0L
        }
        if (total > 0) {
            "₹" + total.toString().reversed().chunked(3).joinToString(",").reversed()
        } else {
            "₹0"
        }
    }
    var toastMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {
        AppTopBar(
            title = "Partner Payouts",
            subtitle = "70:30 Commission Distribution",
            onBackClick = onBack
        )

        toastMessage?.let { msg ->
            AppToastBanner(
                message = msg,
                type = ToastType.SUCCESS,
                modifier = Modifier.padding(bottom = 10.dp),
                onDismiss = { toastMessage = null }
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                // Blue Payout Summary Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
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
                            .padding(22.dp)
                    ) {
                        Text(
                            text = "TOTAL DISBURSEMENT QUEUE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xCCFFFFFF),
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = totalQueueAmountFormatted,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Scheduled for 30 Sep · Direct NEFT/IMPS",
                            fontSize = 12.5.sp,
                            color = Color(0xDDFFFFFF)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Awaiting Release (${payouts.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            items(payouts) { item ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFF8FAFC),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(Color.White, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                BankIcon(size = 20.dp, color = BrandBluePrimary)
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.agentName,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${item.agentCode} · ${item.period}",
                                    fontSize = 12.5.sp,
                                    color = TextMuted
                                )
                            }

                            Text(
                                text = item.amount,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandBluePrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.bankDetails,
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )

                            if (item.status == "Pending") {
                                Surface(
                                    onClick = {
                                        onProcessPayout(item.id)
                                        toastMessage = "Disbursed ${item.amount} to ${item.agentName}"
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFF16A34A)
                                ) {
                                    Text(
                                        text = "Process Payout",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            } else {
                                Text(
                                    text = "✓ Disbursed",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF16A34A)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

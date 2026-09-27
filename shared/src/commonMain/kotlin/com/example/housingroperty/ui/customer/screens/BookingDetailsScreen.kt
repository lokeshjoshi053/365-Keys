package com.example.housingroperty.ui.customer.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import com.example.housingroperty.data.models.Booking
import com.example.housingroperty.data.models.Milestone
import com.example.housingroperty.data.models.MilestoneStatus
import com.example.housingroperty.ui.components.AppToastBanner
import com.example.housingroperty.ui.components.AppTopBar
import com.example.housingroperty.ui.components.CheckmarkIcon
import com.example.housingroperty.ui.components.ToastType
import com.example.housingroperty.ui.customer.components.StatusBadge
import com.example.housingroperty.ui.theme.*

@Composable
fun BookingDetailsScreen(
    booking: Booking,
    onBack: () -> Unit,
    showPaymentSuccessBanner: Boolean = false,
    modifier: Modifier = Modifier
) {
    var showSuccessToast by remember { mutableStateOf(showPaymentSuccessBanner) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {
        AppTopBar(onBackClick = onBack, title = "Booking Details")

        if (showSuccessToast) {
            AppToastBanner(
                message = "Payment verified successfully",
                type = ToastType.SUCCESS,
                modifier = Modifier.padding(bottom = 12.dp),
                onDismiss = { showSuccessToast = false }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Blue Status Hero Card
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
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${booking.plotTitle.uppercase()} · ${booking.planName.uppercase().replace(" TIER", "")}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xCCFFFFFF),
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = booking.stepText,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = booking.stepSummary,
                        fontSize = 13.sp,
                        color = Color(0xDDFFFFFF)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Milestones Timeline
            booking.milestones.forEach { milestone ->
                MilestoneRow(milestone = milestone)
                Spacer(modifier = Modifier.height(14.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun MilestoneRow(
    milestone: Milestone,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Milestone indicator
            when (milestone.status) {
                MilestoneStatus.COMPLETED -> {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE2E8F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "✓", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                }
                MilestoneStatus.IN_PROGRESS -> {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, Color(0xFF94A3B8), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "⏱", fontSize = 11.sp)
                    }
                }
                MilestoneStatus.UPCOMING -> {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, Color(0xFFCBD5E1), CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = milestone.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = milestone.subtitle,
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }

            if (milestone.badgeLabel != null) {
                StatusBadge(label = milestone.badgeLabel)
            }
        }
    }
}

package com.example.housingroperty.ui.customer.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.data.models.Booking
import com.example.housingroperty.ui.customer.components.*
import com.example.housingroperty.ui.theme.*

@Composable
fun CustomerHomeScreen(
    userName: String,
    activeBooking: Booking?,
    onNotificationsClick: () -> Unit,
    onBrowsePlotsClick: () -> Unit,
    onDocumentsClick: () -> Unit,
    onPaymentsClick: () -> Unit,
    onBookingCardClick: () -> Unit,
    modifier: Modifier = Modifier,
    onOpenAgentDashboard: () -> Unit = {},
    onOpenAdminDashboard: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {
        // Top Header
        com.example.housingroperty.ui.components.AppTopBar(
            title = "Good morning, $userName",
            subtitle = "Verified inventory & live plots",
            actions = {
                com.example.housingroperty.ui.components.ActionButton(
                    onClick = onNotificationsClick,
                    hasBadge = true
                ) {
                    AlertsNavIcon(size = 20.dp, color = TextPrimary)
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Active Booking Hero Card
            activeBooking?.let { booking ->
                Surface(
                    onClick = onBookingCardClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(20.dp),
                            spotColor = BrandBluePrimary.copy(alpha = 0.35f)
                        ),
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
                            text = "ACTIVE BOOKING",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xCCFFFFFF),
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${booking.plotTitle} · ${booking.planName}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Payment 2 of 3 completed",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xDDFFFFFF)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Explore Section Label
            Text(
                text = "EXPLORE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSubtle,
                letterSpacing = 0.8.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Explore Card 1: Browse Plots
            ExploreCard(
                icon = { PlotsNavIcon(size = 22.dp, color = BrandBluePrimary) },
                title = "Browse Plots",
                subtitle = "315 plots · Bhopal Ring Rd",
                onClick = onBrowsePlotsClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Explore Card 2: Documents
            ExploreCard(
                icon = { DocsNavIcon(size = 22.dp, color = BrandBluePrimary) },
                title = "Documents",
                subtitle = "1 needs your signature",
                badge = "Action needed",
                onClick = onDocumentsClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Explore Card 3: Payment History
            ExploreCard(
                icon = {
                    Text(
                        text = "💳",
                        fontSize = 18.sp
                    )
                },
                title = "Payment History",
                subtitle = "3 transactions",
                onClick = onPaymentsClick
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun ExploreCard(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    badge: String? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }

            if (badge != null) {
                StatusBadge(label = badge)
                Spacer(modifier = Modifier.width(8.dp))
            }

            ArrowRightIcon(size = 16.dp, color = Color(0xFF94A3B8))
        }
    }
}

package com.example.housingroperty.ui.agent.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.housingroperty.data.models.DashboardStats
import com.example.housingroperty.ui.agent.components.MetricCard
import com.example.housingroperty.ui.customer.components.AlertsNavIcon
import com.example.housingroperty.ui.customer.components.ArrowRightIcon
import com.example.housingroperty.ui.theme.*

@Composable
fun PartnerDashboardScreen(
    stats: DashboardStats,
    onNotificationsClick: () -> Unit,
    onShareReferralClick: () -> Unit,
    onRegisterLeadClick: () -> Unit,
    onEarningsClick: () -> Unit,
    modifier: Modifier = Modifier,
    onMatchingClick: () -> Unit = {},
    onPayoutClick: () -> Unit = {},
    onOpenCustomerHome: () -> Unit = {},
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
            title = "Partner Dashboard",
            subtitle = "Overview & active network",
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

            // Blue Hero Card
            Surface(
                onClick = onEarningsClick,
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
                        text = "THIS MONTH",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xCCFFFFFF),
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "${stats.netCommission} Net Commission",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "${stats.activeLeadsCount} active leads",
                        fontSize = 13.sp,
                        color = Color(0xDDFFFFFF)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2x2 Stats Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                MetricCard(
                    value = stats.leadsCount,
                    label = "Leads",
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    value = stats.bookingsCount,
                    label = "Bookings",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                MetricCard(
                    value = stats.matchedVolume,
                    label = "Matched Vol.",
                    modifier = Modifier.weight(1f),
                    onClick = onMatchingClick
                )
                MetricCard(
                    value = stats.payoutDue,
                    label = "Payout Due",
                    modifier = Modifier.weight(1f),
                    onClick = onPayoutClick
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Action Card 1: Share Referral Link
            Surface(
                onClick = onShareReferralClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF8FAFC),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🔗", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Share Referral Link",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    ArrowRightIcon(size = 16.dp, color = BrandBluePrimary)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Card 2: Register New Lead
            Surface(
                onClick = onRegisterLeadClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF8FAFC),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "＋", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BrandBluePrimary)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Register New Lead",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    ArrowRightIcon(size = 16.dp, color = BrandBluePrimary)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

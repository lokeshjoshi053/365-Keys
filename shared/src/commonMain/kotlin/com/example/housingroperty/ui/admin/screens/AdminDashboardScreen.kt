package com.example.housingroperty.ui.admin.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.data.models.AdminActivityLog
import com.example.housingroperty.data.models.AdminOverviewStats
import com.example.housingroperty.ui.components.AppTopBar
import com.example.housingroperty.ui.customer.components.ArrowRightIcon
import com.example.housingroperty.ui.theme.*

@Composable
fun AdminDashboardScreen(
    stats: AdminOverviewStats,
    activities: List<AdminActivityLog>,
    onNavigateToPlots: () -> Unit,
    onNavigateToApprovals: () -> Unit,
    onNavigateToPayouts: () -> Unit,
    modifier: Modifier = Modifier,
    onOpenCustomerView: () -> Unit = {},
    onOpenAgentView: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {
        AppTopBar(
            title = "Admin Console",
            subtitle = "Master Platform Control"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Blue Master Hero Card
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
                                listOf(Color(0xFF1D58E2), Color(0xFF0F3BB0))
                            )
                        )
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "TOTAL PLATFORM VOLUME",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xCCFFFFFF),
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stats.totalRevenue,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "${stats.totalPlots} Plots · ${stats.activePartners} Active Partners",
                        fontSize = 13.sp,
                        color = Color(0xDDFFFFFF)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4 Grid Metric Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    modifier = Modifier.weight(1f),
                    value = "${stats.plotsSold} Sold",
                    label = "Plots Inventory",
                    color = Color(0xFF0F172A),
                    onClick = onNavigateToPlots
                )
                MetricCard(
                    modifier = Modifier.weight(1f),
                    value = "${stats.plotsAvailable} Free",
                    label = "Ready to Book",
                    color = Color(0xFF16A34A),
                    onClick = onNavigateToPlots
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    modifier = Modifier.weight(1f),
                    value = "${stats.pendingApprovals} Pending",
                    label = "Approvals Queue",
                    color = Color(0xFFD97706),
                    onClick = onNavigateToApprovals
                )
                MetricCard(
                    modifier = Modifier.weight(1f),
                    value = stats.pendingPayouts,
                    label = "Payouts Due",
                    color = BrandBluePrimary,
                    onClick = onNavigateToPayouts
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            // Quick Operations shortcuts
            Text(
                text = "Operations Hub",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            OperationRow(
                title = "Pending Approvals (${stats.pendingApprovals})",
                subtitle = "Review partner KYC and booking agreements",
                badge = "${stats.pendingApprovals} Actionable",
                badgeColor = Color(0xFFFEF3C7),
                badgeTextColor = Color(0xFFB45309),
                onClick = onNavigateToApprovals
            )

            Spacer(modifier = Modifier.height(12.dp))

            OperationRow(
                title = "Inventory Management",
                subtitle = "Update plot availability, sectors, and prices",
                badge = "${stats.totalPlots} Total",
                badgeColor = Color(0xFFEFF6FF),
                badgeTextColor = BrandBluePrimary,
                onClick = onNavigateToPlots
            )

            Spacer(modifier = Modifier.height(12.dp))

            OperationRow(
                title = "Process Commission Payouts",
                subtitle = "Approve verified 70:30 matching balances",
                badge = "Cycle Due",
                badgeColor = Color(0xFFDCFCE7),
                badgeTextColor = Color(0xFF15803D),
                onClick = onNavigateToPayouts
            )

            Spacer(modifier = Modifier.height(26.dp))

            // Recent Activity Section
            Text(
                text = "Live Audit Trail",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            activities.forEach { log ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF8FAFC),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = log.title,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = log.timeAgo,
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = log.description,
                            fontSize = 13.sp,
                            color = Color(0xFF64748B),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun MetricCard(
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(86.dp),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextMuted
            )
        }
    }
}

@Composable
private fun OperationRow(
    title: String,
    subtitle: String,
    badge: String,
    badgeColor: Color,
    badgeTextColor: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = badgeColor
                    ) {
                        Text(
                            text = badge,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeTextColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.5.sp,
                    color = TextMuted
                )
            }

            ArrowRightIcon(size = 14.dp, color = BrandBluePrimary)
        }
    }
}

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.data.models.AdminApprovalItem
import com.example.housingroperty.data.models.ApprovalType
import com.example.housingroperty.ui.components.AppToastBanner
import com.example.housingroperty.ui.components.AppTopBar
import com.example.housingroperty.ui.components.EmptyStateView
import com.example.housingroperty.ui.components.ToastType
import com.example.housingroperty.ui.theme.*

@Composable
fun AdminApprovalsScreen(
    approvals: List<AdminApprovalItem>,
    onApprove: (String) -> Unit,
    onReject: (String) -> Unit,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("Pending") }
    var toastMessage by remember { mutableStateOf<String?>(null) }

    val filteredList = remember(approvals, selectedFilter) {
        if (selectedFilter == "All") approvals else approvals.filter { it.status.equals(selectedFilter, ignoreCase = true) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {
        AppTopBar(
            title = "Approvals Queue",
            subtitle = "${approvals.count { it.status == "Pending" }} awaiting review",
            onBackClick = onBack
        )

        // Toast feedback
        toastMessage?.let { msg ->
            AppToastBanner(
                message = msg,
                type = ToastType.SUCCESS,
                modifier = Modifier.padding(bottom = 10.dp),
                onDismiss = { toastMessage = null }
            )
        }

        // Filter Pills Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Pending", "All", "Approved").forEach { filter ->
                val isSelected = selectedFilter == filter
                Surface(
                    onClick = { selectedFilter = filter },
                    shape = RoundedCornerShape(50),
                    color = if (isSelected) BrandBluePrimary else Color.White,
                    border = if (isSelected) null else BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = filter,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color(0xFF64748B)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (filteredList.isEmpty()) {
            EmptyStateView(
                title = "No $selectedFilter Approvals",
                subtitle = "Great job! All submitted partner KYC verification and booking agreements are up to date."
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredList) { item ->
                    ApprovalCard(
                        item = item,
                        onApprove = {
                            onApprove(item.id)
                            toastMessage = "Approved ${item.title}"
                        },
                        onReject = {
                            onReject(item.id)
                            toastMessage = "Rejected ${item.title}"
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ApprovalCard(
    item: AdminApprovalItem,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    val (badgeBg, badgeText) = when (item.type) {
        ApprovalType.PARTNER_KYC -> Color(0xFFEFF6FF) to BrandBluePrimary
        ApprovalType.BOOKING_AGREEMENT -> Color(0xFFF0FDF4) to Color(0xFF16A34A)
        ApprovalType.BANK_UPDATE -> Color(0xFFFEF3C7) to Color(0xFFD97706)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = badgeBg
                ) {
                    Text(
                        text = item.type.label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeText,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                    )
                }

                Text(
                    text = item.submittedAt,
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = item.title,
                fontSize = 15.5.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = item.subtitle,
                fontSize = 13.sp,
                color = Color(0xFF64748B),
                lineHeight = 18.sp
            )

            if (item.status == "Pending") {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        onClick = onReject,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFFEE2E2),
                        border = BorderStroke(1.dp, Color(0xFFFECACA))
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "Reject",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFDC2626)
                            )
                        }
                    }

                    Surface(
                        onClick = onApprove,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF16A34A)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "Approve",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Status: ${item.status}",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (item.status == "Approved") Color(0xFF16A34A) else Color(0xFFDC2626)
                )
            }
        }
    }
}

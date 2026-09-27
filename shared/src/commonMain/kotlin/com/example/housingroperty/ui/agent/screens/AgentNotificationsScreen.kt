package com.example.housingroperty.ui.agent.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import com.example.housingroperty.data.models.AgentNotification
import com.example.housingroperty.ui.agent.components.BankIcon
import com.example.housingroperty.ui.agent.components.EarningsNavIcon
import com.example.housingroperty.ui.components.CustomerAgentIcon
import com.example.housingroperty.ui.components.EmptyStateView
import com.example.housingroperty.ui.theme.*

@Composable
fun AgentNotificationsScreen(
    notifications: List<AgentNotification>,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var notifList by remember { mutableStateOf(notifications) }
    val permissionController = com.example.housingroperty.permissions.rememberPermissionController()
    var notifPermissionStatus by remember {
        mutableStateOf(permissionController.getPermissionStatus(com.example.housingroperty.permissions.PermissionType.NOTIFICATION))
    }
    var showNotifRationale by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {
        com.example.housingroperty.ui.components.AppTopBar(
            title = "Notifications",
            subtitle = if (notifList.isEmpty()) "All caught up" else "${notifList.size} updates",
            onBackClick = onBack,
            actions = {
                if (notifList.isNotEmpty()) {
                    androidx.compose.material3.TextButton(onClick = { notifList = emptyList() }) {
                        Text("Clear", color = BrandBluePrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                } else {
                    androidx.compose.material3.TextButton(onClick = { notifList = notifications }) {
                        Text("Restore", color = BrandBluePrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        )

        if (notifPermissionStatus != com.example.housingroperty.permissions.PermissionStatus.GRANTED) {
            com.example.housingroperty.permissions.NotificationPermissionBanner(
                controller = permissionController,
                onEnableClicked = { showNotifRationale = true }
            )
        }

        if (notifList.isEmpty()) {
            EmptyStateView(
                title = "You're all caught up",
                subtitle = "New booking, payment and document updates will show up here."
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(notifList) { notif ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFF8FAFC),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            when (notif.type) {
                                "commission" -> EarningsNavIcon(size = 22.dp, color = Color(0xFF64748B))
                                "lead" -> CustomerAgentIcon(size = 20.dp, color = Color(0xFF64748B))
                                else -> BankIcon(size = 20.dp, color = Color(0xFF64748B))
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = notif.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = notif.timeAgo,
                                fontSize = 13.sp,
                                color = TextMuted
                            )
                        }
                    }
                }
            }
        }
    }
}

    if (showNotifRationale) {
        com.example.housingroperty.permissions.PermissionRationaleDialog(
            permissionType = com.example.housingroperty.permissions.PermissionType.NOTIFICATION,
            controller = permissionController,
            onDismiss = { showNotifRationale = false },
            onPermissionResult = { isGranted ->
                if (isGranted) {
                    notifPermissionStatus = com.example.housingroperty.permissions.PermissionStatus.GRANTED
                }
                showNotifRationale = false
            }
        )
    }
}

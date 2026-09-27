package com.example.housingroperty.permissions

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.housingroperty.ui.customer.components.AlertsNavIcon
import com.example.housingroperty.ui.customer.components.MapPinIcon
import com.example.housingroperty.ui.theme.*

@Composable
fun PermissionRationaleDialog(
    permissionType: PermissionType,
    controller: PermissionController,
    onDismiss: () -> Unit,
    onPermissionResult: (Boolean) -> Unit
) {
    var isPermanentlyDenied by remember { mutableStateOf(false) }

    val title = when (permissionType) {
        PermissionType.LOCATION -> "Enable Location Access"
        PermissionType.NOTIFICATION -> "Enable Real-Time Alerts"
    }

    val subtitle = when (permissionType) {
        PermissionType.LOCATION -> "Required to display nearby plots, load project coordinates, and navigate satellite master blueprints."
        PermissionType.NOTIFICATION -> "Stay instantly informed on plot reservations, price adjustments, document verifications, and approvals."
    }

    val features = when (permissionType) {
        PermissionType.LOCATION -> listOf(
            "Discover verified plots nearest to your current location",
            "Interactive satellite blueprint and survey map loading",
            "Accurate route planning and distance calculation"
        )
        PermissionType.NOTIFICATION -> listOf(
            "Instant booking confirmations and plot status changes",
            "Payment receipts, milestone reminders, and registry dates",
            "Partner matching bonuses and payout status notifications"
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .shadow(24.dp, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Icon with subtle ambient gradient
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                if (permissionType == PermissionType.LOCATION)
                                    listOf(Color(0xFFEFF6FF), Color(0xFFDBEAFE))
                                else
                                    listOf(Color(0xFFFEF3C7), Color(0xFFFDE68A))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (permissionType == PermissionType.LOCATION) {
                        MapPinIcon(size = 32.dp, color = BrandBluePrimary)
                    } else {
                        AlertsNavIcon(size = 30.dp, color = Color(0xFFD97706))
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = subtitle,
                    fontSize = 13.5.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Feature Checklist
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8FAFC), RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    features.forEach { feature ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF22C55E)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "✓",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = feature,
                                fontSize = 12.5.sp,
                                color = TextPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Button(
                    onClick = {
                        if (isPermanentlyDenied) {
                            controller.openAppSettings()
                            onDismiss()
                        } else {
                            controller.requestPermission(permissionType) { status ->
                                if (status == PermissionStatus.GRANTED) {
                                    onPermissionResult(true)
                                    onDismiss()
                                } else {
                                    isPermanentlyDenied = true
                                    onPermissionResult(false)
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandBluePrimary
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = if (isPermanentlyDenied) "Open System Settings" else "Allow Access",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Continue Without Access",
                        fontSize = 13.5.sp,
                        color = Color(0xFF64748B),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationPermissionBanner(
    controller: PermissionController,
    onEnableClicked: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFFFFBEB),
        border = BorderStroke(1.dp, Color(0xFFFDE68A))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFEF3C7)),
                contentAlignment = Alignment.Center
            ) {
                AlertsNavIcon(size = 18.dp, color = Color(0xFFD97706))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Notifications are disabled",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF92400E)
                )
                Text(
                    text = "Enable alerts to stay updated on bookings & payouts.",
                    fontSize = 11.5.sp,
                    color = Color(0xFFB45309),
                    lineHeight = 15.sp
                )
            }

            Surface(
                onClick = onEnableClicked,
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFD97706)
            ) {
                Text(
                    text = "Enable",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun PermissionsManagementDialog(
    controller: PermissionController,
    onDismiss: () -> Unit
) {
    var locationStatus by remember { mutableStateOf(controller.getPermissionStatus(PermissionType.LOCATION)) }
    var notificationStatus by remember { mutableStateOf(controller.getPermissionStatus(PermissionType.NOTIFICATION)) }
    var pendingRationale by remember { mutableStateOf<PermissionType?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight()
                .shadow(24.dp, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "App Permissions",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Control access for maps, nearby plot discovery, and alerts.",
                    fontSize = 13.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Location Permission Item
                PermissionRowItem(
                    title = "Location Access",
                    subtitle = "For displaying nearby plots & blueprint maps",
                    icon = { MapPinIcon(size = 22.dp, color = BrandBluePrimary) },
                    status = locationStatus,
                    onRequest = {
                        pendingRationale = PermissionType.LOCATION
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Notification Permission Item
                PermissionRowItem(
                    title = "Push Notifications",
                    subtitle = "For booking updates, milestones & payouts",
                    icon = { AlertsNavIcon(size = 20.dp, color = Color(0xFFD97706)) },
                    status = notificationStatus,
                    onRequest = {
                        pendingRationale = PermissionType.NOTIFICATION
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        controller.openAppSettings()
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Open System Device Settings",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1E293B)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Close",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandBluePrimary
                    )
                }
            }
        }
    }

    pendingRationale?.let { type ->
        PermissionRationaleDialog(
            permissionType = type,
            controller = controller,
            onDismiss = {
                pendingRationale = null
                locationStatus = controller.getPermissionStatus(PermissionType.LOCATION)
                notificationStatus = controller.getPermissionStatus(PermissionType.NOTIFICATION)
            },
            onPermissionResult = {
                pendingRationale = null
                locationStatus = controller.getPermissionStatus(PermissionType.LOCATION)
                notificationStatus = controller.getPermissionStatus(PermissionType.NOTIFICATION)
            }
        )
    }
}

@Composable
private fun PermissionRowItem(
    title: String,
    subtitle: String,
    icon: @Composable () -> Unit,
    status: PermissionStatus,
    onRequest: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 11.5.sp,
                    color = TextSecondary,
                    lineHeight = 15.sp
                )
            }

            if (status == PermissionStatus.GRANTED) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color(0xFFDCFCE7)
                ) {
                    Text(
                        text = "Allowed",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF16A34A)
                    )
                }
            } else {
                Surface(
                    onClick = onRequest,
                    shape = RoundedCornerShape(50),
                    color = BrandBluePrimary
                ) {
                    Text(
                        text = "Allow",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

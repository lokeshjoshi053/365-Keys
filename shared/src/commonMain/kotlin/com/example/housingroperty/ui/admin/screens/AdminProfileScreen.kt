package com.example.housingroperty.ui.admin.screens

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.ui.components.CustomerAgentIcon
import com.example.housingroperty.ui.components.LockIcon
import com.example.housingroperty.ui.components.LogoutConfirmDialog
import com.example.housingroperty.ui.customer.components.ArrowRightIcon
import com.example.housingroperty.ui.theme.*

@Composable
fun AdminProfileScreen(
    adminName: String = "System Administrator",
    adminEmail: String = "superadmin@315plots.com",
    onEditProfile: () -> Unit = {},
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    onOpenCustomerView: () -> Unit = {},
    onOpenAgentView: () -> Unit = {}
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showSecurityDialog by remember { mutableStateOf(false) }
    var showAuditDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {
        com.example.housingroperty.ui.components.AppTopBar(
            title = "Settings & Profile",
            subtitle = "Admin Security & Logs",
            onBackClick = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Blue Admin Profile Card
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
                        text = "PLATFORM SUPERADMIN",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xCCFFFFFF),
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = adminName,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = adminEmail,
                        fontSize = 13.sp,
                        color = Color(0xDDFFFFFF)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Account Details",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            AdminOptionRow(
                icon = { CustomerAgentIcon(size = 20.dp, color = Color(0xFF64748B)) },
                label = "Edit Profile",
                onClick = onEditProfile
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "System & Security",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            AdminOptionRow(
                icon = { LockIcon(size = 20.dp, color = Color(0xFF64748B)) },
                label = "Security & Access Control",
                onClick = { showSecurityDialog = true }
            )

            Spacer(modifier = Modifier.height(12.dp))

            AdminOptionRow(
                icon = { Text(text = "🛡", fontSize = 16.sp) },
                label = "Audit Trail & Blockchain Logs",
                onClick = { showAuditDialog = true }
            )

            Spacer(modifier = Modifier.height(12.dp))

            AdminOptionRow(
                icon = { Text(text = "↪", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626)) },
                label = "Logout",
                onClick = { showLogoutDialog = true }
            )

            Spacer(modifier = Modifier.height(28.dp))
        }

        if (showSecurityDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showSecurityDialog = false },
                confirmButton = {
                    androidx.compose.material3.TextButton(onClick = { showSecurityDialog = false }) {
                        Text("Done", color = BrandBluePrimary, fontWeight = FontWeight.Bold)
                    }
                },
                title = { Text("Security & Access Control", fontWeight = FontWeight.Bold) },
                text = {
                    Text("• Role: Super Administrator\n• 2FA Enforcement: Active (Hardware Key + OTP)\n• IP Whitelist: Enabled for 10.0.0.0/16\n• Session Timeout: 30 minutes idle")
                }
            )
        }

        if (showAuditDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showAuditDialog = false },
                confirmButton = {
                    androidx.compose.material3.TextButton(onClick = { showAuditDialog = false }) {
                        Text("Close", color = BrandBluePrimary, fontWeight = FontWeight.Bold)
                    }
                },
                title = { Text("Audit Trail & Compliance", fontWeight = FontWeight.Bold) },
                text = {
                    Text("• Immutable Audit Hash: 0x8f2a...e91b (Chain validated)\n• Total log entries: 1,482 today\n• Compliance checks: SOC2 Type II, RERA Section 11 verified\n• Backup sync: Real-time multi-AZ replicated")
                }
            )
        }

        if (showLogoutDialog) {
            LogoutConfirmDialog(
                onDismiss = { showLogoutDialog = false },
                onConfirmLogout = {
                    showLogoutDialog = false
                    onLogout()
                }
            )
        }
    }
}

@Composable
private fun AdminOptionRow(
    icon: @Composable () -> Unit,
    label: String,
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
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(28.dp),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = label,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )

            ArrowRightIcon(size = 16.dp, color = BrandBluePrimary)
        }
    }
}

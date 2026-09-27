package com.example.housingroperty.ui.agent.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.runtime.*
import com.example.housingroperty.data.models.PartnerProfile
import com.example.housingroperty.ui.agent.components.BankIcon
import com.example.housingroperty.ui.components.CustomerAgentIcon
import com.example.housingroperty.ui.components.LockIcon
import com.example.housingroperty.ui.components.LogoutConfirmDialog
import com.example.housingroperty.ui.customer.components.ArrowRightIcon
import com.example.housingroperty.ui.theme.*

@Composable
fun AgentProfileScreen(
    profile: PartnerProfile,
    onEditProfile: () -> Unit,
    onBankDetails: () -> Unit,
    onSecurity: () -> Unit,
    onLogout: () -> Unit,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    val permissionController = com.example.housingroperty.permissions.rememberPermissionController()
    var showPermissionsDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {
        com.example.housingroperty.ui.components.AppTopBar(
            title = "Partner Profile",
            subtitle = "Agent ID: ${profile.agentCode}",
            onBackClick = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            // Blue Partner Card
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
                        text = profile.fullName.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xCCFFFFFF),
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = profile.roleTitle,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${profile.status} · ID ${profile.agentCode}",
                        fontSize = 13.sp,
                        color = Color(0xDDFFFFFF)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Action Options
            AgentOptionRow(
                icon = { CustomerAgentIcon(size = 20.dp, color = Color(0xFF64748B)) },
                label = "Edit Profile",
                onClick = onEditProfile
            )

            Spacer(modifier = Modifier.height(14.dp))

            AgentOptionRow(
                icon = { BankIcon(size = 20.dp, color = Color(0xFF64748B)) },
                label = "Bank Details",
                onClick = onBankDetails
            )

            Spacer(modifier = Modifier.height(14.dp))

            AgentOptionRow(
                icon = { LockIcon(size = 20.dp, color = Color(0xFF64748B)) },
                label = "Security",
                onClick = onSecurity
            )

            Spacer(modifier = Modifier.height(14.dp))

            AgentOptionRow(
                icon = { com.example.housingroperty.ui.customer.components.MapPinIcon(size = 20.dp, color = Color(0xFF64748B)) },
                label = "App Permissions (Location & Alerts)",
                onClick = { showPermissionsDialog = true }
            )

            Spacer(modifier = Modifier.height(14.dp))

            AgentOptionRow(
                icon = { Text(text = "↪", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626)) },
                label = "Logout",
                onClick = { showLogoutDialog = true }
            )

            Spacer(modifier = Modifier.height(28.dp))
        }

        if (showPermissionsDialog) {
            com.example.housingroperty.permissions.PermissionsManagementDialog(
                controller = permissionController,
                onDismiss = { showPermissionsDialog = false }
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
private fun AgentOptionRow(
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

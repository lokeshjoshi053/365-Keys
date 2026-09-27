package com.example.housingroperty.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.ui.components.*
import com.example.housingroperty.ui.theme.*

enum class UserRole(
    val title: String,
    val description: String,
    val badgeLabel: String,
    val shortName: String
) {
    CUSTOMER(
        title = "Customer / Buyer",
        description = "Browse & book verified plots, track milestone progress, view legal documents, and make installments",
        badgeLabel = "Continuing as Customer",
        shortName = "Customer"
    ),
    AGENT(
        title = "Channel Partner / Agent",
        description = "Register buyer clients, monitor deals, earn tiered commission, and request direct bank payouts",
        badgeLabel = "Continuing as Channel Partner",
        shortName = "Partner"
    ),
    ADMIN(
        title = "Platform Admin",
        description = "Manage plots inventory, update pricing, verify KYC agreements, and process agent payouts",
        badgeLabel = "Continuing as Admin",
        shortName = "Admin"
    );

    companion object {
        val CUSTOMER_AGENT: UserRole get() = CUSTOMER
    }
}

@Composable
fun RoleSelectionScreen(
    onBack: () -> Unit,
    onRoleSelected: (UserRole) -> Unit,
    onDirectNavigate: ((UserRole) -> Unit)? = null,
    modifier: Modifier = Modifier,
    initialRole: UserRole = UserRole.CUSTOMER
) {
    var selectedRole by remember { mutableStateOf(initialRole) }
    val scrollState = rememberScrollState()

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {
        val isLandscape = maxWidth > maxHeight || maxWidth > 650.dp

        Column(modifier = Modifier.fillMaxSize()) {
            // Back navigation bar
            TopBarBack(onBackClick = onBack)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = if (isLandscape) 32.dp else 24.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Title
                Text(
                    text = "Choose Your Role",
                    fontSize = if (isLandscape) 22.sp else 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Subtitle
                Text(
                    text = "Select how you would like to enter the 315 Plots platform",
                    fontSize = if (isLandscape) 13.sp else 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(if (isLandscape) 18.dp else 28.dp))

                if (isLandscape) {
                    // ==========================================
                    // LANDSCAPE LAYOUT: 3 Cards Side-by-Side in Row
                    // ==========================================
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        UserRole.entries.forEach { role ->
                            LandscapeRoleCard(
                                role = role,
                                isSelected = selectedRole == role,
                                onClick = { selectedRole = role },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                } else {
                    // ==========================================
                    // PORTRAIT LAYOUT: Stacked Cards
                    // ==========================================
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        UserRole.entries.forEach { role ->
                            PortraitRoleCard(
                                role = role,
                                isSelected = selectedRole == role,
                                onClick = { selectedRole = role }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(if (isLandscape) 20.dp else 32.dp))

                // Bottom Action Buttons
                if (isLandscape) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Option 1: Direct Enter Dashboard
                        if (onDirectNavigate != null) {
                            Surface(
                                onClick = { onDirectNavigate(selectedRole) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFEFF6FF),
                                border = BorderStroke(1.dp, Color(0xFFBFDBFE))
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "Direct ${selectedRole.shortName} Portal ➔",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BrandBluePrimary
                                    )
                                }
                            }
                        }

                        // Option 2: Continue with Login
                        PrimaryButton(
                            text = "Continue with Phone Login",
                            onClick = { onRoleSelected(selectedRole) },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        PrimaryButton(
                            text = "Continue as ${selectedRole.shortName}",
                            onClick = { onRoleSelected(selectedRole) }
                        )

                        if (onDirectNavigate != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                onClick = { onDirectNavigate(selectedRole) },
                                shape = RoundedCornerShape(50),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                            ) {
                                Text(
                                    text = "⚡ Direct Launch ${selectedRole.shortName} Dashboard",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = BrandBluePrimary,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PortraitRoleCard(
    role: UserRole,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) BrandBluePrimary else Color(0xFFE2E8F0)
    val backgroundColor = if (isSelected) Color(0xFFF7F9FE) else Color.White

    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = backgroundColor,
        border = BorderStroke(if (isSelected) 1.8.dp else 1.dp, borderColor),
        shadowElevation = if (isSelected) 3.dp else 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon container
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) Color(0xFFEBF2FE) else Color(0xFFF1F5F9)),
                contentAlignment = Alignment.Center
            ) {
                RoleIcon(role = role, isSelected = isSelected)
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Text Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = role.title,
                    fontSize = 16.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = role.description,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextMuted,
                    lineHeight = 17.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Radio / Checkbox Indicator
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .then(
                        if (isSelected) Modifier.background(BrandBluePrimary)
                        else Modifier.border(1.5.dp, Color(0xFFCBD5E1), CircleShape)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    CheckmarkIcon(size = 14.dp, color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun LandscapeRoleCard(
    role: UserRole,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) BrandBluePrimary else Color(0xFFE2E8F0)
    val backgroundColor = if (isSelected) Color(0xFFF7F9FE) else Color.White

    Surface(
        onClick = onClick,
        modifier = modifier.heightIn(min = 160.dp),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor),
        shadowElevation = if (isSelected) 3.dp else 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) Color(0xFFEBF2FE) else Color(0xFFF1F5F9)),
                    contentAlignment = Alignment.Center
                ) {
                    RoleIcon(role = role, isSelected = isSelected)
                }

                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .then(
                            if (isSelected) Modifier.background(BrandBluePrimary)
                            else Modifier.border(1.5.dp, Color(0xFFCBD5E1), CircleShape)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        CheckmarkIcon(size = 12.dp, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = role.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = role.description,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Normal,
                color = TextMuted,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun RoleIcon(role: UserRole, isSelected: Boolean) {
    val tint = if (isSelected) BrandBluePrimary else Color(0xFF475569)
    when (role) {
        UserRole.CUSTOMER -> CustomerBuyerIcon(size = 22.dp, color = tint)
        UserRole.AGENT -> PartnerAgentIcon(size = 22.dp, color = tint)
        UserRole.ADMIN -> ShieldAdminIcon(size = 22.dp, color = tint)
    }
}

package com.example.housingroperty.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.ui.components.CheckmarkIcon
import com.example.housingroperty.ui.components.PillBadge
import com.example.housingroperty.ui.components.PrimaryButton
import com.example.housingroperty.ui.theme.*

@Composable
fun OnboardingSuccessScreen(
    role: UserRole,
    onRestartFlow: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {
        val isLandscape = maxWidth > maxHeight || maxWidth > 650.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = if (isLandscape) 48.dp else 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Success Green Circle
            Box(
                modifier = Modifier
                    .size(if (isLandscape) 64.dp else 80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8FDF3)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(if (isLandscape) 42.dp else 52.dp)
                        .clip(CircleShape)
                        .background(SuccessGreen),
                    contentAlignment = Alignment.Center
                ) {
                    CheckmarkIcon(size = if (isLandscape) 22.dp else 28.dp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(if (isLandscape) 14.dp else 24.dp))

            // Role Badge
            PillBadge(
                text = role.badgeLabel,
                backgroundColor = Color(0xFFEFF6FF),
                textColor = BrandBluePrimary
            )

            Spacer(modifier = Modifier.height(if (isLandscape) 12.dp else 18.dp))

            // Title
            Text(
                text = "Welcome to 315 PLOTS!",
                fontSize = if (isLandscape) 22.sp else 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = when (role) {
                    UserRole.CUSTOMER -> "Your customer account is verified. You can now browse verified plots, book plots, track payment milestones, and view legal documents."
                    UserRole.AGENT -> "Your partner account is ready. You can now register client leads, track plot commissions, and request direct payouts."
                    UserRole.ADMIN -> "Your administrator console is ready. You have master access to manage inventory, approve partner KYC, and oversee operations."
                },
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Normal,
                color = TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 21.sp,
                modifier = Modifier.widthIn(max = 500.dp)
            )

            Spacer(modifier = Modifier.height(if (isLandscape) 20.dp else 36.dp))

            // Primary Navigation Button
            PrimaryButton(
                text = "Enter ${role.shortName} Portal",
                onClick = onRestartFlow,
                modifier = Modifier.widthIn(max = 400.dp)
            )
        }
    }
}

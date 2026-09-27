package com.example.housingroperty.ui.admin.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.ui.theme.BrandBluePrimary

enum class AdminTab(val label: String) {
    OVERVIEW("Overview"),
    PLOTS("Plots"),
    APPROVALS("Approvals"),
    PAYOUTS("Payouts"),
    SETTINGS("Settings")
}

@Composable
fun DashboardNavIcon(size: Dp = 20.dp, color: Color = Color(0xFF64748B)) {
    Canvas(modifier = Modifier.size(size)) {
        val s = this.size.minDimension
        val strokeW = 1.8.dp.toPx()
        val corner = 2.dp.toPx()

        drawRoundRect(color, Offset(s * 0.15f, s * 0.15f), Size(s * 0.32f, s * 0.32f), CornerRadius(corner, corner), Stroke(strokeW))
        drawRoundRect(color, Offset(s * 0.53f, s * 0.15f), Size(s * 0.32f, s * 0.32f), CornerRadius(corner, corner), Stroke(strokeW))
        drawRoundRect(color, Offset(s * 0.15f, s * 0.53f), Size(s * 0.32f, s * 0.32f), CornerRadius(corner, corner), Stroke(strokeW))
        drawRoundRect(color, Offset(s * 0.53f, s * 0.53f), Size(s * 0.32f, s * 0.32f), CornerRadius(corner, corner), Stroke(strokeW))
    }
}

@Composable
fun ApprovalsNavIcon(size: Dp = 20.dp, color: Color = Color(0xFF64748B)) {
    Canvas(modifier = Modifier.size(size)) {
        val s = this.size.minDimension
        val strokeW = 1.8.dp.toPx()

        // Clipboard Outline
        val clipPath = Path().apply {
            moveTo(s * 0.25f, s * 0.25f)
            lineTo(s * 0.25f, s * 0.85f)
            lineTo(s * 0.75f, s * 0.85f)
            lineTo(s * 0.75f, s * 0.25f)
            close()
        }
        drawPath(clipPath, color, style = Stroke(strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))

        // Clip Top
        drawRoundRect(color, Offset(s * 0.36f, s * 0.12f), Size(s * 0.28f, s * 0.16f), CornerRadius(2.dp.toPx(), 2.dp.toPx()), Stroke(strokeW))

        // Checkmark inside
        val check = Path().apply {
            moveTo(s * 0.35f, s * 0.52f)
            lineTo(s * 0.46f, s * 0.65f)
            lineTo(s * 0.65f, s * 0.42f)
        }
        drawPath(check, color, style = Stroke(strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

@Composable
fun PayoutsNavIcon(size: Dp = 20.dp, color: Color = Color(0xFF64748B)) {
    Canvas(modifier = Modifier.size(size)) {
        val s = this.size.minDimension
        val strokeW = 1.8.dp.toPx()

        // Card body
        drawRoundRect(color, Offset(s * 0.15f, s * 0.25f), Size(s * 0.7f, s * 0.52f), CornerRadius(4.dp.toPx(), 4.dp.toPx()), Stroke(strokeW))
        // Magnetic line
        drawLine(color, Offset(s * 0.15f, s * 0.42f), Offset(s * 0.85f, s * 0.42f), strokeWidth = strokeW)
        // Chip
        drawCircle(color, radius = s * 0.08f, center = Offset(s * 0.35f, s * 0.6f), style = Stroke(strokeW))
    }
}

@Composable
fun SettingsNavIcon(size: Dp = 20.dp, color: Color = Color(0xFF64748B)) {
    Canvas(modifier = Modifier.size(size)) {
        val s = this.size.minDimension
        val strokeW = 1.8.dp.toPx()

        // Outer slider lines
        drawLine(color, Offset(s * 0.2f, s * 0.3f), Offset(s * 0.8f, s * 0.3f), strokeWidth = strokeW, cap = StrokeCap.Round)
        drawCircle(color, radius = s * 0.1f, center = Offset(s * 0.4f, s * 0.3f), style = Stroke(strokeW))

        drawLine(color, Offset(s * 0.2f, s * 0.7f), Offset(s * 0.8f, s * 0.7f), strokeWidth = strokeW, cap = StrokeCap.Round)
        drawCircle(color, radius = s * 0.1f, center = Offset(s * 0.65f, s * 0.7f), style = Stroke(strokeW))
    }
}

@Composable
fun AdminBottomBar(
    currentTab: AdminTab,
    pendingApprovalsCount: Int = 0,
    onTabSelected: (AdminTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 12.dp, spotColor = Color(0x1A000000)),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AdminTab.entries.forEach { tab ->
                val isSelected = currentTab == tab
                val itemColor = if (isSelected) BrandBluePrimary else Color(0xFF64748B)

                Column(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onTabSelected(tab) }
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(contentAlignment = Alignment.TopEnd) {
                        when (tab) {
                            AdminTab.OVERVIEW -> DashboardNavIcon(size = 20.dp, color = itemColor)
                            AdminTab.PLOTS -> com.example.housingroperty.ui.customer.components.PlotsNavIcon(size = 20.dp, color = itemColor)
                            AdminTab.APPROVALS -> ApprovalsNavIcon(size = 20.dp, color = itemColor)
                            AdminTab.PAYOUTS -> PayoutsNavIcon(size = 20.dp, color = itemColor)
                            AdminTab.SETTINGS -> SettingsNavIcon(size = 20.dp, color = itemColor)
                        }

                        if (tab == AdminTab.APPROVALS && pendingApprovalsCount > 0) {
                            Box(
                                modifier = Modifier
                                    .offset(x = 6.dp, y = (-4).dp)
                                    .size(8.dp)
                                    .shadow(2.dp, shape = CircleShape)
                            ) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    drawCircle(Color(0xFFEF4444))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = tab.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = itemColor
                    )
                }
            }
        }
    }
}

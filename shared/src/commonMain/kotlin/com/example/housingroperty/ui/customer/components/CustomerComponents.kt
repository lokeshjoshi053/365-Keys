package com.example.housingroperty.ui.customer.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.ui.theme.*

enum class CustomerTab(val label: String) {
    HOME("Home"),
    PLOTS("Plots"),
    DOCS("Docs"),
    ALERTS("Alerts"),
    PROFILE("Profile")
}

@Composable
fun CustomerBottomBar(
    currentTab: CustomerTab,
    onTabSelected: (CustomerTab) -> Unit,
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
            CustomerTab.entries.forEach { tab ->
                val isSelected = currentTab == tab
                val itemColor = if (isSelected) BrandBluePrimary else Color(0xFF64748B)

                Column(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onTabSelected(tab) }
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    when (tab) {
                        CustomerTab.HOME -> HomeNavIcon(size = 20.dp, color = itemColor)
                        CustomerTab.PLOTS -> PlotsNavIcon(size = 20.dp, color = itemColor)
                        CustomerTab.DOCS -> DocsNavIcon(size = 20.dp, color = itemColor)
                        CustomerTab.ALERTS -> AlertsNavIcon(size = 20.dp, color = itemColor)
                        CustomerTab.PROFILE -> ProfileNavIcon(size = 20.dp, color = itemColor)
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

@Composable
fun StatusBadge(
    status: com.example.housingroperty.data.models.PlotStatus,
    modifier: Modifier = Modifier
) {
    StatusBadge(label = status.label, modifier = modifier)
}

@Composable
fun StatusBadge(
    label: String,
    modifier: Modifier = Modifier
) {
    val (bg, textColor) = when (label.lowercase()) {
        "available" -> Color(0xFFE8FDF3) to Color(0xFF16A34A)
        "sold" -> Color(0xFFFEE2E2) to Color(0xFFDC2626)
        "popular" -> Color(0xFF1D58E2) to Color.White
        "action needed", "pending", "sent", "on hold" -> Color(0xFFFEF3C7) to Color(0xFFD97706)
        "signed", "verified" -> Color(0xFFE8FDF3) to Color(0xFF16A34A)
        "customer" -> Color(0xFFEFF6FF) to Color(0xFF1D58E2)
        else -> Color(0xFFF1F5F9) to Color(0xFF475569)
    }

    Box(
        modifier = modifier
            .background(bg, RoundedCornerShape(50.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}

// Icons for Customer Panel
@Composable
fun HomeNavIcon(size: Dp = 20.dp, color: Color = Color(0xFF64748B)) {
    Canvas(modifier = Modifier.size(size)) {
        val s = this.size.minDimension
        val path = Path().apply {
            moveTo(s * 0.15f, s * 0.45f)
            lineTo(s * 0.5f, s * 0.15f)
            lineTo(s * 0.85f, s * 0.45f)
            lineTo(s * 0.85f, s * 0.85f)
            lineTo(s * 0.15f, s * 0.85f)
            close()
        }
        drawPath(path, color = color, style = Stroke(width = 1.8.dp.toPx(), join = StrokeJoin.Round))
    }
}

@Composable
fun PlotsNavIcon(size: Dp = 20.dp, color: Color = Color(0xFF64748B)) {
    Canvas(modifier = Modifier.size(size)) {
        val s = this.size.minDimension
        val path = Path().apply {
            moveTo(s * 0.15f, s * 0.25f)
            lineTo(s * 0.4f, s * 0.15f)
            lineTo(s * 0.6f, s * 0.25f)
            lineTo(s * 0.85f, s * 0.15f)
            lineTo(s * 0.85f, s * 0.75f)
            lineTo(s * 0.6f, s * 0.85f)
            lineTo(s * 0.4f, s * 0.75f)
            lineTo(s * 0.15f, s * 0.85f)
            close()
        }
        drawPath(path, color = color, style = Stroke(width = 1.8.dp.toPx(), join = StrokeJoin.Round))
        drawLine(color, Offset(s * 0.4f, s * 0.15f), Offset(s * 0.4f, s * 0.75f), strokeWidth = 1.5.dp.toPx())
        drawLine(color, Offset(s * 0.6f, s * 0.25f), Offset(s * 0.6f, s * 0.85f), strokeWidth = 1.5.dp.toPx())
    }
}

@Composable
fun DocsNavIcon(size: Dp = 20.dp, color: Color = Color(0xFF64748B)) {
    Canvas(modifier = Modifier.size(size)) {
        val s = this.size.minDimension
        val path = Path().apply {
            moveTo(s * 0.2f, s * 0.15f)
            lineTo(s * 0.6f, s * 0.15f)
            lineTo(s * 0.8f, s * 0.35f)
            lineTo(s * 0.8f, s * 0.85f)
            lineTo(s * 0.2f, s * 0.85f)
            close()
        }
        drawPath(path, color = color, style = Stroke(width = 1.8.dp.toPx(), join = StrokeJoin.Round))
        drawLine(color, Offset(s * 0.35f, s * 0.45f), Offset(s * 0.65f, s * 0.45f), strokeWidth = 1.5.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(s * 0.35f, s * 0.62f), Offset(s * 0.65f, s * 0.62f), strokeWidth = 1.5.dp.toPx(), cap = StrokeCap.Round)
    }
}

@Composable
fun AlertsNavIcon(size: Dp = 20.dp, color: Color = Color(0xFF64748B)) {
    Canvas(modifier = Modifier.size(size)) {
        val s = this.size.minDimension
        val path = Path().apply {
            moveTo(s * 0.5f, s * 0.15f)
            cubicTo(s * 0.35f, s * 0.15f, s * 0.28f, s * 0.35f, s * 0.28f, s * 0.55f)
            lineTo(s * 0.2f, s * 0.72f)
            lineTo(s * 0.8f, s * 0.72f)
            lineTo(s * 0.72f, s * 0.55f)
            cubicTo(s * 0.72f, s * 0.35f, s * 0.65f, s * 0.15f, s * 0.5f, s * 0.15f)
        }
        drawPath(path, color = color, style = Stroke(width = 1.8.dp.toPx(), join = StrokeJoin.Round))
        drawCircle(color, radius = s * 0.08f, center = Offset(s * 0.5f, s * 0.83f))
    }
}

@Composable
fun ProfileNavIcon(size: Dp = 20.dp, color: Color = Color(0xFF64748B)) {
    Canvas(modifier = Modifier.size(size)) {
        val s = this.size.minDimension
        drawCircle(color, radius = s * 0.2f, center = Offset(s * 0.5f, s * 0.32f), style = Stroke(width = 1.8.dp.toPx()))
        val body = Path().apply {
            moveTo(s * 0.22f, s * 0.82f)
            cubicTo(s * 0.25f, s * 0.6f, s * 0.75f, s * 0.6f, s * 0.78f, s * 0.82f)
        }
        drawPath(body, color = color, style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round))
    }
}

@Composable
fun MapPinIcon(size: Dp = 22.dp, color: Color = BrandBluePrimary) {
    Canvas(modifier = Modifier.size(size)) {
        val s = this.size.minDimension
        val path = Path().apply {
            moveTo(s * 0.5f, s * 0.9f)
            cubicTo(s * 0.25f, s * 0.65f, s * 0.2f, s * 0.5f, s * 0.2f, s * 0.38f)
            cubicTo(s * 0.2f, s * 0.2f, s * 0.33f, s * 0.1f, s * 0.5f, s * 0.1f)
            cubicTo(s * 0.67f, s * 0.1f, s * 0.8f, s * 0.2f, s * 0.8f, s * 0.38f)
            cubicTo(s * 0.8f, s * 0.5f, s * 0.75f, s * 0.65f, s * 0.5f, s * 0.9f)
        }
        drawPath(path, color = color, style = Stroke(width = 1.8.dp.toPx(), join = StrokeJoin.Round))
        drawCircle(color, radius = s * 0.12f, center = Offset(s * 0.5f, s * 0.38f))
    }
}

@Composable
fun FilterIcon(size: Dp = 20.dp, color: Color = Color(0xFF1E293B)) {
    Canvas(modifier = Modifier.size(size)) {
        val s = this.size.minDimension
        drawLine(color, Offset(s * 0.15f, s * 0.3f), Offset(s * 0.85f, s * 0.3f), strokeWidth = 1.8.dp.toPx(), cap = StrokeCap.Round)
        drawCircle(color, radius = s * 0.1f, center = Offset(s * 0.4f, s * 0.3f))

        drawLine(color, Offset(s * 0.15f, s * 0.7f), Offset(s * 0.85f, s * 0.7f), strokeWidth = 1.8.dp.toPx(), cap = StrokeCap.Round)
        drawCircle(color, radius = s * 0.1f, center = Offset(s * 0.65f, s * 0.7f))
    }
}

@Composable
fun ExternalShareIcon(size: Dp = 20.dp, color: Color = Color(0xFF1E293B)) {
    Canvas(modifier = Modifier.size(size)) {
        val s = this.size.minDimension
        drawLine(color, Offset(s * 0.35f, s * 0.65f), Offset(s * 0.75f, s * 0.25f), strokeWidth = 1.8.dp.toPx(), cap = StrokeCap.Round)
        val arrow = Path().apply {
            moveTo(s * 0.45f, s * 0.25f)
            lineTo(s * 0.75f, s * 0.25f)
            lineTo(s * 0.75f, s * 0.55f)
        }
        drawPath(arrow, color = color, style = Stroke(width = 1.8.dp.toPx(), join = StrokeJoin.Round, cap = StrokeCap.Round))
    }
}

@Composable
fun ArrowRightIcon(size: Dp = 16.dp, color: Color = Color(0xFF94A3B8)) {
    Canvas(modifier = Modifier.size(size)) {
        val s = this.size.minDimension
        val path = Path().apply {
            moveTo(s * 0.35f, s * 0.2f)
            lineTo(s * 0.65f, s * 0.5f)
            lineTo(s * 0.35f, s * 0.8f)
        }
        drawPath(path, color = color, style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

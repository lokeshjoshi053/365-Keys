package com.example.housingroperty.ui.agent.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.ui.customer.components.HomeNavIcon
import com.example.housingroperty.ui.customer.components.ProfileNavIcon
import com.example.housingroperty.ui.theme.BrandBluePrimary
import com.example.housingroperty.ui.theme.TextMuted
import com.example.housingroperty.ui.theme.TextPrimary

enum class AgentTab(val label: String) {
    HOME("Home"),
    LEADS("Leads"),
    NETWORK("Network"),
    EARNINGS("Earnings"),
    PROFILE("Profile")
}

@Composable
fun AgentBottomBar(
    currentTab: AgentTab,
    onTabSelected: (AgentTab) -> Unit,
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
            AgentTab.entries.forEach { tab ->
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
                        AgentTab.HOME -> HomeNavIcon(size = 20.dp, color = itemColor)
                        AgentTab.LEADS -> LeadsNavIcon(size = 20.dp, color = itemColor)
                        AgentTab.NETWORK -> NetworkNavIcon(size = 20.dp, color = itemColor)
                        AgentTab.EARNINGS -> EarningsNavIcon(size = 20.dp, color = itemColor)
                        AgentTab.PROFILE -> ProfileNavIcon(size = 20.dp, color = itemColor)
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
fun MetricCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Surface(
        onClick = { onClick?.invoke() },
        enabled = onClick != null,
        modifier = modifier.height(76.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                color = TextMuted
            )
        }
    }
}

// Agent Icons
@Composable
fun LeadsNavIcon(size: Dp = 20.dp, color: Color = Color(0xFF64748B)) {
    Canvas(modifier = Modifier.size(size)) {
        val s = this.size.minDimension
        // Two users
        drawCircle(color, radius = s * 0.16f, center = Offset(s * 0.38f, s * 0.32f), style = Stroke(width = 1.8.dp.toPx()))
        drawCircle(color, radius = s * 0.14f, center = Offset(s * 0.72f, s * 0.36f), style = Stroke(width = 1.5.dp.toPx()))

        val body1 = Path().apply {
            moveTo(s * 0.15f, s * 0.82f)
            cubicTo(s * 0.18f, s * 0.6f, s * 0.58f, s * 0.6f, s * 0.62f, s * 0.82f)
        }
        drawPath(body1, color = color, style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round))
    }
}

@Composable
fun NetworkNavIcon(size: Dp = 20.dp, color: Color = Color(0xFF64748B)) {
    Canvas(modifier = Modifier.size(size)) {
        val s = this.size.minDimension
        // Tree nodes: top node, 2 children
        drawCircle(color, radius = s * 0.12f, center = Offset(s * 0.5f, s * 0.22f), style = Stroke(width = 1.8.dp.toPx()))
        drawCircle(color, radius = s * 0.11f, center = Offset(s * 0.25f, s * 0.75f), style = Stroke(width = 1.8.dp.toPx()))
        drawCircle(color, radius = s * 0.11f, center = Offset(s * 0.75f, s * 0.75f), style = Stroke(width = 1.8.dp.toPx()))

        // Connector lines
        drawLine(color, Offset(s * 0.45f, s * 0.34f), Offset(s * 0.3f, s * 0.64f), strokeWidth = 1.5.dp.toPx())
        drawLine(color, Offset(s * 0.55f, s * 0.34f), Offset(s * 0.7f, s * 0.64f), strokeWidth = 1.5.dp.toPx())
    }
}

@Composable
fun EarningsNavIcon(size: Dp = 20.dp, color: Color = Color(0xFF64748B)) {
    Canvas(modifier = Modifier.size(size)) {
        val s = this.size.minDimension
        // Coin circle with rupee symbol or badge
        drawCircle(color, radius = s * 0.38f, center = Offset(s * 0.5f, s * 0.5f), style = Stroke(width = 1.8.dp.toPx()))
        drawLine(color, Offset(s * 0.35f, s * 0.35f), Offset(s * 0.65f, s * 0.35f), strokeWidth = 1.5.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(s * 0.35f, s * 0.47f), Offset(s * 0.65f, s * 0.47f), strokeWidth = 1.5.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(s * 0.5f, s * 0.35f), Offset(s * 0.5f, s * 0.68f), strokeWidth = 1.5.dp.toPx(), cap = StrokeCap.Round)
    }
}

@Composable
fun BankIcon(size: Dp = 20.dp, color: Color = Color(0xFF64748B)) {
    Canvas(modifier = Modifier.size(size)) {
        val s = this.size.minDimension
        // Roof triangle
        val roof = Path().apply {
            moveTo(s * 0.15f, s * 0.38f)
            lineTo(s * 0.5f, s * 0.15f)
            lineTo(s * 0.85f, s * 0.38f)
            close()
        }
        drawPath(roof, color = color, style = Stroke(width = 1.8.dp.toPx(), join = StrokeJoin.Round))

        // Columns
        drawLine(color, Offset(s * 0.25f, s * 0.42f), Offset(s * 0.25f, s * 0.75f), strokeWidth = 1.8.dp.toPx())
        drawLine(color, Offset(s * 0.5f, s * 0.42f), Offset(s * 0.5f, s * 0.75f), strokeWidth = 1.8.dp.toPx())
        drawLine(color, Offset(s * 0.75f, s * 0.42f), Offset(s * 0.75f, s * 0.75f), strokeWidth = 1.8.dp.toPx())

        // Base line
        drawLine(color, Offset(s * 0.12f, s * 0.82f), Offset(s * 0.88f, s * 0.82f), strokeWidth = 2.dp.toPx(), cap = StrokeCap.Round)
    }
}

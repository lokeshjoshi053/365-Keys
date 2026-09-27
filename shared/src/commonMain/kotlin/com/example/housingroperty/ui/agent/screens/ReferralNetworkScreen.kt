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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.data.models.NetworkLevel
import com.example.housingroperty.ui.components.CustomerAgentIcon
import com.example.housingroperty.ui.customer.components.ArrowRightIcon
import com.example.housingroperty.ui.theme.*

@Composable
fun ReferralNetworkScreen(
    levels: List<NetworkLevel>,
    onViewTreeClick: () -> Unit,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {
        com.example.housingroperty.ui.components.AppTopBar(
            title = "Referral Network",
            subtitle = "${levels.size} active tiers",
            onBackClick = onBack,
            actions = {
                Surface(
                    onClick = onViewTreeClick,
                    shape = RoundedCornerShape(50.dp),
                    color = Color(0xFFEFF6FF),
                    border = BorderStroke(1.dp, Color(0xFFBFDBFE))
                ) {
                    Text(
                        text = "View Tree  →",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandBluePrimary,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            // Dynamic Level Cards Grid
            val levelChunks = levels.chunked(2)
            levelChunks.forEachIndexed { index, pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    pair.forEach { levelItem ->
                        LevelCard(
                            members = "${levelItem.membersCount} members",
                            level = "Level ${levelItem.level}",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                if (index < levelChunks.size - 1) {
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Sample member link to tree
            Surface(
                onClick = onViewTreeClick,
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
                        CustomerAgentIcon(size = 20.dp, color = Color(0xFF64748B))
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Priya Sharma",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Level 1 · 2 bookings",
                            fontSize = 13.sp,
                            color = TextMuted
                        )
                    }

                    ArrowRightIcon(size = 16.dp, color = BrandBluePrimary)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun LevelCard(
    members: String,
    level: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(84.dp),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = members,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = level,
                fontSize = 12.sp,
                color = TextMuted
            )
        }
    }
}

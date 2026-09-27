package com.example.housingroperty.ui.agent.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.data.models.TreeNode
import com.example.housingroperty.ui.components.CustomerAgentIcon
import com.example.housingroperty.ui.components.TopBarBack
import com.example.housingroperty.ui.customer.components.StatusBadge
import com.example.housingroperty.ui.theme.*

@Composable
fun ReferralTreeScreen(
    rootNode: TreeNode,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {
        TopBarBack(onBackClick = onBack, title = "Referral Tree")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Root Node (Blue Card)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(20.dp),
                        spotColor = BrandBluePrimary.copy(alpha = 0.35f)
                    ),
                shape = RoundedCornerShape(20.dp),
                color = BrandBluePrimary
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF1D58E2), Color(0xFF1443B8))
                            )
                        )
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0x33FFFFFF)),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomerAgentIcon(size = 22.dp, color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = rootNode.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = rootNode.subtitle,
                            fontSize = 12.sp,
                            color = Color(0xCCFFFFFF)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = Color(0x33FFFFFF)
                    ) {
                        Text(
                            text = rootNode.badgeText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tree Children
            rootNode.children.forEach { child ->
                TreeNodeItem(node = child, depth = 0)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun TreeNodeItem(
    node: TreeNode,
    depth: Int
) {
    val indent = (depth * 20).dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = indent, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Tree Connector line
        if (depth > 0) {
            Box(
                modifier = Modifier
                    .width(16.dp)
                    .height(2.dp)
                    .background(Color(0xFFCBD5E1))
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFF8FAFC),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(BrandBluePrimary),
                    contentAlignment = Alignment.Center
                ) {
                    CustomerAgentIcon(size = 18.dp, color = Color.White)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = node.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = node.subtitle,
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Color(0xFFEFF6FF),
                    border = BorderStroke(1.dp, Color(0xFFBFDBFE))
                ) {
                    Text(
                        text = node.badgeText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandBluePrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }
    }

    node.children.forEach { subChild ->
        TreeNodeItem(node = subChild, depth = depth + 1)
    }
}

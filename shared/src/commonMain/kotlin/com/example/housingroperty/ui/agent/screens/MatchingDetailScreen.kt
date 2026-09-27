package com.example.housingroperty.ui.agent.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.data.models.MatchingSummary
import com.example.housingroperty.ui.components.TopBarBack
import com.example.housingroperty.ui.theme.*

@Composable
fun MatchingDetailScreen(
    matching: MatchingSummary,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {
        TopBarBack(onBackClick = onBack, title = "Matching Detail")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 2x2 Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                MatchingBox(
                    value = matching.matchedVolShort,
                    label = "Matched Vol.",
                    modifier = Modifier.weight(1f)
                )
                MatchingBox(
                    value = matching.gross50,
                    label = "Gross (50%)",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                MatchingBox(
                    value = matching.admin20,
                    label = "Admin (20%)",
                    valueColor = Color(0xFFDC2626),
                    modifier = Modifier.weight(1f)
                )
                MatchingBox(
                    value = matching.net,
                    label = "Net",
                    valueColor = BrandBluePrimary,
                    isBold = true,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MatchingBox(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    valueColor: Color = TextPrimary,
    isBold: Boolean = false
) {
    Surface(
        modifier = modifier.height(78.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontSize = 17.sp,
                fontWeight = if (isBold) FontWeight.ExtraBold else FontWeight.Bold,
                color = valueColor
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

package com.example.housingroperty.ui.customer.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.data.models.BookingPlan
import com.example.housingroperty.ui.components.TopBarBack
import com.example.housingroperty.ui.customer.components.StatusBadge
import com.example.housingroperty.ui.theme.*

@Composable
fun PlanSelectionScreen(
    plans: List<BookingPlan>,
    onBack: () -> Unit,
    onPlanSelected: (BookingPlan) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {
        TopBarBack(onBackClick = onBack, title = "Choose your plan")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            plans.forEach { plan ->
                PlanCard(
                    plan = plan,
                    onClick = { onPlanSelected(plan) }
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PlanCard(
    plan: BookingPlan,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, if (plan.isPopular) BrandBluePrimary else Color(0xFFE2E8F0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = plan.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    if (plan.isPopular) {
                        Spacer(modifier = Modifier.width(10.dp))
                        StatusBadge(label = "Popular")
                    }
                }
            }

            Text(
                text = plan.amountFormatted,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = BrandBluePrimary
            )
        }
    }
}

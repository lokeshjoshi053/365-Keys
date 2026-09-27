package com.example.housingroperty.ui.customer.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.data.models.BookingPlan
import com.example.housingroperty.data.models.Plot
import com.example.housingroperty.ui.components.CheckmarkIcon
import com.example.housingroperty.ui.components.PrimaryButton
import com.example.housingroperty.ui.components.TopBarBack
import com.example.housingroperty.ui.customer.components.MapPinIcon
import com.example.housingroperty.ui.theme.*

@Composable
fun BookingReviewScreen(
    plot: Plot,
    plan: BookingPlan,
    onBack: () -> Unit,
    onProceedToPayment: () -> Unit,
    modifier: Modifier = Modifier
) {
    var agreedToTerms by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {
        TopBarBack(onBackClick = onBack, title = "Review Booking")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Plot Info Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFFF8FAFC),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        MapPinIcon(size = 22.dp, color = Color(0xFF64748B))
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = plot.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "${plot.block} · ${plot.sizeSqFt}",
                            fontSize = 13.sp,
                            color = TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Plan Info Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFFF8FAFC),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🏷️", fontSize = 18.sp)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "${plan.name} Tier",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "${plan.amountFormatted} down payment",
                            fontSize = 13.sp,
                            color = TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Terms Agreement Checkbox
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { agreedToTerms = !agreedToTerms }
                    )
                    .background(Color(0xFFF8FAFC), RoundedCornerShape(14.dp))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .then(
                            if (agreedToTerms) Modifier.background(BrandBluePrimary)
                            else Modifier.border(1.5.dp, Color(0xFFCBD5E1), RoundedCornerShape(6.dp))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (agreedToTerms) {
                        CheckmarkIcon(size = 12.dp, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "I agree to the Terms & Booking Conditions",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Proceed to Payment Button
            PrimaryButton(
                text = "Proceed to Payment",
                onClick = onProceedToPayment,
                enabled = agreedToTerms,
                modifier = Modifier.padding(bottom = 24.dp, top = 20.dp)
            )
        }
    }
}

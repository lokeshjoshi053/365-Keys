package com.example.housingroperty.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.ui.components.PrimaryButton
import com.example.housingroperty.ui.components.TopBarBack
import com.example.housingroperty.ui.theme.TextMuted
import com.example.housingroperty.ui.theme.TextPrimary

data class TermSection(val number: Int, val title: String, val body: String)

val TermsList = listOf(
    TermSection(
        number = 1,
        title = "Token & Booking",
        body = "Express and Advance tokens hold your selected plot for a limited period (15–30 days). A registered agreement with the applicable minimum down payment must be completed within this window to retain the booking."
    ),
    TermSection(
        number = 2,
        title = "Cancellation",
        body = "If the balance amount isn't paid within the agreed timeline, the token may be forfeited and any related referral commission adjusted accordingly."
    ),
    TermSection(
        number = 3,
        title = "Referral Commission & Deductions",
        body = "All commission and 70:30 matching bonus payouts are calculated net of a 20% Admin/Service charge and 5% Government TDS before being credited to the registered bank account."
    ),
    TermSection(
        number = 4,
        title = "Payout Cycle",
        body = "Commission is released only after the payment is verified and credited to the company account. Annual Form 16A TDS certificates are issued to agents."
    ),
    TermSection(
        number = 5,
        title = "Allotment & Possession",
        body = "Plot demarcations and registry execution will proceed in accordance with local municipal authority approvals and verified survey settlement maps."
    )
)

@Composable
fun TermsAndConditionsScreen(
    onBack: () -> Unit,
    onAccept: () -> Unit = onBack,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {
        // Back Navigation with Title
        TopBarBack(
            onBackClick = onBack,
            title = "Terms & Conditions"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            TermsList.forEach { section ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Text(
                        text = "${section.number}. ${section.title}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = section.body,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = TextMuted,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = "I Understand & Accept",
                onClick = onAccept,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
    }
}

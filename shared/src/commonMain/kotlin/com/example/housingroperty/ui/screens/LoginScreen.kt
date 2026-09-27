package com.example.housingroperty.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.ui.components.*
import com.example.housingroperty.ui.theme.*

@Composable
fun LoginScreen(
    role: UserRole,
    onBack: () -> Unit,
    onContinueGoogle: () -> Unit,
    onSendOtp: (String) -> Unit,
    onOpenTerms: () -> Unit,
    onOpenPrivacy: () -> Unit,
    modifier: Modifier = Modifier,
    initialPhoneNumber: String = "9876543210"
) {
    var phoneNumber by remember { mutableStateOf(initialPhoneNumber) }
    var agreedToTerms by remember { mutableStateOf(true) }
    val isPhoneValid = phoneNumber.filter { it.isDigit() }.length == 10
    val canSubmit = isPhoneValid && agreedToTerms

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
            .imePadding()
    ) {
        // Back Navigation
        TopBarBack(onBackClick = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Role Badge Pill
            PillBadge(
                text = role.badgeLabel,
                backgroundColor = Color(0xFFEFF6FF),
                textColor = BrandBluePrimary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Title
            Text(
                text = "Welcome back",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Subtitle
            Text(
                text = "Sign in to continue to your account",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = TextMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Continue with Google Button
            GoogleSignInButton(
                onClick = onContinueGoogle
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Divider: "or continue with mobile"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFE2E8F0)
                )
                Text(
                    text = "  or continue with mobile  ",
                    fontSize = 13.sp,
                    color = TextSubtle,
                    fontWeight = FontWeight.Medium
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFE2E8F0)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Mobile number input section
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Mobile number",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Input Box with Country Code and Flag
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .border(
                            width = 1.dp,
                            color = if (isPhoneValid) BrandBluePrimary else Color(0xFFE2E8F0),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .background(Color(0xFFFAFAFA))
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IndiaFlagIcon(width = 24.dp, height = 16.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "+91",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(20.dp)
                            .background(Color(0xFFCBD5E1))
                    )
                    Spacer(modifier = Modifier.width(12.dp))

                    BasicTextField(
                        value = phoneNumber,
                        onValueChange = { input ->
                            val digitsOnly = input.filter { it.isDigit() }
                            val cleanNumber = when {
                                digitsOnly.startsWith("91") && digitsOnly.length > 10 -> digitsOnly.removePrefix("91")
                                digitsOnly.startsWith("0") && digitsOnly.length > 10 -> digitsOnly.removePrefix("0")
                                else -> digitsOnly
                            }.take(10)
                            phoneNumber = cleanNumber
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary,
                            letterSpacing = 1.2.sp
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (canSubmit) onSendOtp(phoneNumber)
                            }
                        ),
                        cursorBrush = SolidColor(BrandBluePrimary),
                        decorationBox = { innerTextField ->
                            if (phoneNumber.isEmpty()) {
                                Text(
                                    text = "98•••• ••210",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = TextSubtle,
                                    letterSpacing = 1.2.sp
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Checkbox and Agreement Text
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { agreedToTerms = !agreedToTerms }
                    ),
                verticalAlignment = Alignment.Top
            ) {
                // Custom Checkbox
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

                // Legal Disclaimer Text with interactive links
                val annotatedAgreement = remember {
                    buildAnnotatedString {
                        append("I agree to the ")
                        val termsLink = androidx.compose.ui.text.LinkAnnotation.Clickable(
                            tag = "TERMS",
                            styles = androidx.compose.ui.text.TextLinkStyles(
                                style = SpanStyle(
                                    color = BrandBluePrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    textDecoration = TextDecoration.Underline
                                )
                            ),
                            linkInteractionListener = { onOpenTerms() }
                        )
                        pushLink(termsLink)
                        append("Terms & Conditions")
                        pop()

                        append(" and ")

                        val privacyLink = androidx.compose.ui.text.LinkAnnotation.Clickable(
                            tag = "PRIVACY",
                            styles = androidx.compose.ui.text.TextLinkStyles(
                                style = SpanStyle(
                                    color = BrandBluePrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    textDecoration = TextDecoration.Underline
                                )
                            ),
                            linkInteractionListener = { onOpenPrivacy() }
                        )
                        pushLink(privacyLink)
                        append("Privacy Policy")
                        pop()

                        append(" of 315 PLOTS.")
                    }
                }

                Text(
                    text = annotatedAgreement,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Primary Button: "Send OTP"
            PrimaryButton(
                text = "Send OTP",
                onClick = { onSendOtp(phoneNumber) },
                enabled = canSubmit,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
    }
}

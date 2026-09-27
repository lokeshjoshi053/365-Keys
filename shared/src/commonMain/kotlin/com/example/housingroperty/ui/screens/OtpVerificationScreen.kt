package com.example.housingroperty.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.ui.components.*
import com.example.housingroperty.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun OtpVerificationScreen(
    phoneNumber: String,
    onBack: () -> Unit,
    onVerifySuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var otpCode by remember { mutableStateOf("") }
    var secondsLeft by remember { mutableStateOf(28) }
    var isVerifying by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    // Masked phone number representation (e.g., +91 98•••• ••210)
    val displayPhone = remember(phoneNumber) {
        val clean = phoneNumber.filter { it.isDigit() }
        if (clean.length >= 10) {
            val prefix = clean.take(2)
            val suffix = clean.takeLast(3)
            "+91 $prefix•••• ••$suffix"
        } else {
            "+91 98•••• ••210"
        }
    }

    // Countdown Timer Effect
    LaunchedEffect(secondsLeft) {
        if (secondsLeft > 0) {
            delay(1000)
            secondsLeft -= 1
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Lock Icon Badge Container
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEFF6FF)),
                contentAlignment = Alignment.Center
            ) {
                LockIcon(
                    size = 32.dp,
                    color = BrandBluePrimary
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Title
            Text(
                text = "Verify your number",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Masked Phone Info
            Text(
                text = "Code sent to $displayPhone",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = TextMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Hidden BasicTextField capturing keyboard inputs
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    value = otpCode,
                    onValueChange = { input ->
                        if (input.length <= 6 && input.all { it.isDigit() }) {
                            otpCode = input
                            if (input.length == 6) {
                                // Auto verify trigger or user presses button
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (otpCode.length == 6) {
                                isVerifying = true
                                onVerifySuccess()
                            }
                        }
                    ),
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .size(1.dp)
                )

                // 6 OTP Digit Box UI
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { focusRequester.requestFocus() }
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0 until 6) {
                        val digit = otpCode.getOrNull(i)?.toString() ?: ""
                        val isCurrentBox = otpCode.length == i
                        val isFilled = digit.isNotEmpty()

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .padding(horizontal = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFAFAFA))
                                .border(
                                    width = if (isCurrentBox) 2.dp else 1.dp,
                                    color = if (isCurrentBox) BrandBluePrimary
                                    else if (isFilled) Color(0xFF64748B)
                                    else Color(0xFFE2E8F0),
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isFilled) digit else if (isCurrentBox) "" else "•",
                                fontSize = if (isFilled) 20.sp else 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isFilled) TextPrimary else TextSubtle,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Resend OTP Countdown / Action
            if (secondsLeft > 0) {
                val formattedTime = "00:" + secondsLeft.toString().padStart(2, '0')
                Text(
                    text = "Resend OTP in $formattedTime",
                    fontSize = 14.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )
            } else {
                Text(
                    text = "Resend OTP",
                    fontSize = 14.sp,
                    color = BrandBluePrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                secondsLeft = 30
                                otpCode = ""
                            }
                        )
                        .padding(8.dp)
                )
            }

            // Quick Demo helper button
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                onClick = { otpCode = "412580" },
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF1F5F9)
            ) {
                Text(
                    text = "Fill demo: 412580",
                    fontSize = 12.sp,
                    color = TextMuted,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Primary "Verify & Continue" Button
            PrimaryButton(
                text = "Verify & Continue",
                onClick = {
                    isVerifying = true
                    onVerifySuccess()
                },
                enabled = otpCode.length == 6,
                isLoading = isVerifying,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
    }
}

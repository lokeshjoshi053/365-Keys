package com.example.housingroperty.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.ui.components.BlueprintPlotGrid
import com.example.housingroperty.ui.components.PillBadge
import com.example.housingroperty.ui.components.PrimaryButton
import com.example.housingroperty.ui.theme.*

@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit,
    onAlreadyHaveAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1B5AE3))
            .safeDrawingPadding()
    ) {
        val isLandscape = maxWidth > maxHeight || maxWidth > 650.dp

        if (isLandscape) {
            // ==========================================
            // LANDSCAPE LAYOUT: Side-by-Side Split
            // ==========================================
            Row(modifier = Modifier.fillMaxSize()) {
                // Left Half: Blueprint Grid
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    BlueprintPlotGrid(
                        modifier = Modifier.fillMaxSize(),
                        primaryColor = Color(0xFF1B5AE3),
                        gridLineColor = Color(0x30FFFFFF)
                    )

                    PillBadge(
                        text = "315 Plots · Ring Rd",
                        backgroundColor = Color(0x3D0B2056),
                        textColor = Color.White,
                        borderColor = Color(0x33FFFFFF),
                        modifier = Modifier
                            .padding(top = 16.dp, start = 20.dp)
                            .align(Alignment.TopStart)
                    )
                }

                // Right Half: White Card Surface with Scrollable Content
                Surface(
                    modifier = Modifier
                        .weight(1.2f)
                        .fillMaxHeight()
                        .shadow(elevation = 16.dp, spotColor = Color(0x22000000)),
                    shape = RoundedCornerShape(topStart = 28.dp, bottomStart = 28.dp),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 28.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Find your plot.\nBook it with confidence.",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center,
                            lineHeight = 30.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Browse verified inventory, choose a plan and pay securely — every step backed by real-time status.",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Normal,
                            color = TextMuted,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        PrimaryButton(
                            text = "Get Started",
                            onClick = onGetStarted,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "I already have an account",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BrandBluePrimary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onAlreadyHaveAccount
                                )
                                .padding(vertical = 6.dp, horizontal = 12.dp)
                        )
                    }
                }
            }
        } else {
            // ==========================================
            // PORTRAIT LAYOUT
            // ==========================================
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.1f)
                ) {
                    BlueprintPlotGrid(
                        modifier = Modifier.fillMaxSize(),
                        primaryColor = Color(0xFF1B5AE3),
                        gridLineColor = Color(0x30FFFFFF)
                    )

                    PillBadge(
                        text = "315 Plots · Ring Rd",
                        backgroundColor = Color(0x3D0B2056),
                        textColor = Color.White,
                        borderColor = Color(0x33FFFFFF),
                        modifier = Modifier
                            .padding(top = 16.dp, start = 20.dp)
                            .align(Alignment.TopStart)
                    )
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 16.dp,
                            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                            spotColor = Color(0x22000000)
                        ),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Find your plot.\nBook it with confidence.",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center,
                            lineHeight = 32.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Browse verified inventory, choose a plan and pay securely — every step backed by real-time status.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = TextMuted,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        PrimaryButton(
                            text = "Get Started",
                            onClick = onGetStarted
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "I already have an account",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BrandBluePrimary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onAlreadyHaveAccount
                                )
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

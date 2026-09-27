package com.example.housingroperty.ui.customer.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.data.models.Plot
import com.example.housingroperty.ui.components.PrimaryButton
import com.example.housingroperty.ui.components.TopBarBack
import com.example.housingroperty.ui.customer.components.ExternalShareIcon
import com.example.housingroperty.ui.customer.components.MapPinIcon
import com.example.housingroperty.ui.theme.*

@Composable
fun PlotDetailsScreen(
    plot: Plot,
    onBack: () -> Unit,
    onViewOnMap: () -> Unit,
    onSelectPlan: () -> Unit,
    modifier: Modifier = Modifier
) {
    val permissionController = com.example.housingroperty.permissions.rememberPermissionController()
    var showLocationRationale by remember { mutableStateOf(false) }

    val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
    var showShareToast by remember { mutableStateOf(false) }

    LaunchedEffect(showShareToast) {
        if (showShareToast) {
            kotlinx.coroutines.delay(2000)
            showShareToast = false
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .safeDrawingPadding()
        ) {
            // Top App Bar with Share Icon
            com.example.housingroperty.ui.components.AppTopBar(
                title = plot.title,
                subtitle = "${plot.block} · ${plot.location}",
                onBackClick = onBack,
                actions = {
                    com.example.housingroperty.ui.components.ActionButton(
                        onClick = {
                            clipboardManager.setText(
                                androidx.compose.ui.text.AnnotatedString(
                                    "Check out ${plot.title} at ${plot.location} (${plot.sizeSqFt}) - ${plot.totalPriceFormatted}"
                                )
                            )
                            showShareToast = true
                        }
                    ) {
                        ExternalShareIcon(size = 18.dp, color = TextPrimary)
                    }
                }
            )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Hero Blue Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(22.dp),
                        spotColor = BrandBluePrimary.copy(alpha = 0.3f)
                    ),
                shape = RoundedCornerShape(22.dp),
                color = BrandBluePrimary
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF1D58E2), Color(0xFF1443B8))
                            )
                        )
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${plot.block.uppercase()} · ${plot.location.uppercase()}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xCCFFFFFF),
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = plot.sizeSqFt,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (plot.isAvailable) "Available now" else "Sold",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xDDFFFFFF)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 2x2 Specs Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SpecBox(
                    label = "Facing",
                    value = plot.facing,
                    modifier = Modifier.weight(1f)
                )
                SpecBox(
                    label = "Road",
                    value = plot.roadWidth,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SpecBox(
                    label = "Status",
                    value = if (plot.isAvailable) "Open" else "Closed",
                    modifier = Modifier.weight(1f)
                )
                SpecBox(
                    label = "Price/sqft",
                    value = plot.pricePerSqFt,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Township Map & Directions Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Township Map (in-app interactive plan)
                Surface(
                    onClick = {
                        val status = permissionController.getPermissionStatus(com.example.housingroperty.permissions.PermissionType.LOCATION)
                        if (status != com.example.housingroperty.permissions.PermissionStatus.GRANTED) {
                            showLocationRationale = true
                        } else {
                            onViewOnMap()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFEFF6FF),
                    border = BorderStroke(1.dp, Color(0xFFBFDBFE))
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MapPinIcon(size = 18.dp, color = BrandBluePrimary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Township Map",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BrandBluePrimary
                        )
                    }
                }

                // External Directions in Native Maps (Google/Apple Maps)
                Surface(
                    onClick = {
                        permissionController.openExternalMap(
                            latitude = plot.latitude,
                            longitude = plot.longitude,
                            label = "${plot.title} (${plot.block})"
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFF1F5F9),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🧭 Directions",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Select a Plan Primary Button
            PrimaryButton(
                text = "Select a Plan",
                onClick = onSelectPlan,
                enabled = plot.isAvailable,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
    }

        if (showShareToast) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xEE1E293B),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = "Plot details copied to clipboard!",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
        }
    }

    if (showLocationRationale) {
        com.example.housingroperty.permissions.PermissionRationaleDialog(
            permissionType = com.example.housingroperty.permissions.PermissionType.LOCATION,
            controller = permissionController,
            onDismiss = {
                showLocationRationale = false
                onViewOnMap()
            },
            onPermissionResult = {
                showLocationRationale = false
                onViewOnMap()
            }
        )
    }
}

@Composable
private fun SpecBox(
    label: String,
    value: String,
    modifier: Modifier = Modifier
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
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontSize = 17.sp,
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

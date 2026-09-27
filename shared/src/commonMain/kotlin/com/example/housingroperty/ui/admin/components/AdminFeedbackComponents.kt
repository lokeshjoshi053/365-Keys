package com.example.housingroperty.ui.admin.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.housingroperty.ui.components.*
import com.example.housingroperty.ui.theme.BrandBluePrimary

// =========================================================================
// AD15 · CONFIRM DELETE PLOT DIALOG
// =========================================================================

@Composable
fun ConfirmDeletePlotDialog(
    plotTitle: String = "Plot A-114",
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    AppDialog(onDismiss = onDismiss) {
        TrashOutlineIcon(size = 38.dp, color = Color(0xFF0F172A))

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Delete this plot?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "This action cannot be undone. $plotTitle will be permanently removed from listings.",
            fontSize = 13.5.sp,
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center,
            lineHeight = 19.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                onClick = onDismiss,
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFEFF6FF)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Cancel",
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BrandBluePrimary
                    )
                }
            }

            Surface(
                onClick = onConfirmDelete,
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFDC2626)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Delete",
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// =========================================================================
// AD16 · CONFIRM PRICE CHANGE DIALOG
// =========================================================================

@Composable
fun ConfirmPriceChangeDialog(
    plotTitle: String = "Plot A-114",
    oldPrice: String = "₹50,00,000",
    newPrice: String = "₹52,00,000",
    onDismiss: () -> Unit,
    onConfirmUpdate: () -> Unit
) {
    AppDialog(onDismiss = onDismiss) {
        RupeeCoinDialogIcon(size = 38.dp, color = Color(0xFF0F172A))

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Confirm Price Update?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$plotTitle price will change from $oldPrice to $newPrice.",
            fontSize = 13.5.sp,
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center,
            lineHeight = 19.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                onClick = onDismiss,
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFEFF6FF)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Cancel",
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BrandBluePrimary
                    )
                }
            }

            Surface(
                onClick = onConfirmUpdate,
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp),
                shape = RoundedCornerShape(12.dp),
                color = BrandBluePrimary
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Confirm",
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// =========================================================================
// AD17 · CONFIRM STATUS UPDATE DIALOG
// =========================================================================

@Composable
fun ConfirmStatusUpdateDialog(
    plotTitle: String = "Plot A-114",
    targetStatus: String = "Sold",
    onDismiss: () -> Unit,
    onConfirmUpdate: () -> Unit
) {
    AppDialog(onDismiss = onDismiss) {
        SyncRefreshDialogIcon(size = 38.dp, color = Color(0xFF0F172A))

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (targetStatus.equals("Sold", ignoreCase = true)) "Mark Plot as Sold?" else "Mark Plot as Available?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (targetStatus.equals("Sold", ignoreCase = true)) {
                "This will remove $plotTitle from public listings immediately."
            } else {
                "This will restore $plotTitle to public listings for customer bookings."
            },
            fontSize = 13.5.sp,
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center,
            lineHeight = 19.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                onClick = onDismiss,
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFEFF6FF)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Cancel",
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BrandBluePrimary
                    )
                }
            }

            Surface(
                onClick = onConfirmUpdate,
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp),
                shape = RoundedCornerShape(12.dp),
                color = BrandBluePrimary
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Confirm",
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// =========================================================================
// AD18 · PLOT ADDED / ACTION SUCCESS TOAST
// =========================================================================

@Composable
fun AdminPlotToast(
    message: String = "Plot A-115 added successfully",
    modifier: Modifier = Modifier,
    onDismiss: (() -> Unit)? = null
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(14.dp),
                spotColor = Color(0x33000000)
            ),
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFF15803D) // Green tone exactly as in AD18
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CheckmarkIcon(size = 18.dp, color = Color.White)

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = message,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )

            if (onDismiss != null) {
                Text(
                    text = "✕",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier
                        .clickable { onDismiss() }
                        .padding(start = 8.dp)
                )
            }
        }
    }
}

// =========================================================================
// AD14 · EMPTY SEARCH RESULTS VIEW
// =========================================================================

@Composable
fun AdminEmptySearchResultsView(
    title: String = "No matching users",
    subtitle: String = "Try a different name, mobile number or filter combination.",
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 80.dp, bottom = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchOutlineIcon(
                size = 50.dp,
                color = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = title,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = subtitle,
                fontSize = 13.5.sp,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.widthIn(max = 280.dp)
            )

            if (actionText != null && onActionClick != null) {
                Spacer(modifier = Modifier.height(22.dp))
                Surface(
                    onClick = onActionClick,
                    shape = RoundedCornerShape(50),
                    color = Color(0xFFEFF6FF),
                    border = BorderStroke(1.dp, Color(0xFFBFDBFE))
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 9.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = actionText,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BrandBluePrimary
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// AD13 · LOADING TABLE SKELETON
// =========================================================================

@Composable
fun AdminTableSkeletonView(
    count: Int = 4,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition()
    val shimmerAlpha by transition.animateFloat(
        initialValue = 0.40f,
        targetValue = 0.90f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 750, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        repeat(count) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(84.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE2E8F0).copy(alpha = shimmerAlpha))
            )
        }
    }
}

/**
 * Animated Vector Wave Loader (Lottie-like lightweight multiplatform animation)
 */
@Composable
fun AdminLottieLoader(
    modifier: Modifier = Modifier,
    label: String = "Loading inventory..."
) {
    val transition = rememberInfiniteTransition()
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val pulse by transition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(54.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val s = size.minDimension
                val strokeW = 3.5.dp.toPx()
                val radius = (s - strokeW) / 2f
                val center = Offset(s / 2f, s / 2f)

                // Background track
                drawCircle(
                    color = Color(0xFFE2E8F0),
                    radius = radius,
                    center = center,
                    style = Stroke(strokeW)
                )

                // Animated spinning arc
                drawArc(
                    brush = Brush.sweepGradient(
                        listOf(BrandBluePrimary.copy(alpha = 0.1f), BrandBluePrimary)
                    ),
                    startAngle = angle,
                    sweepAngle = 260f,
                    useCenter = false,
                    topLeft = Offset(strokeW / 2, strokeW / 2),
                    size = Size(s - strokeW, s - strokeW),
                    style = Stroke(strokeW)
                )
            }

            Box(
                modifier = Modifier
                    .size((14 * pulse).dp)
                    .clip(CircleShape)
                    .background(BrandBluePrimary)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = label,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF64748B)
        )
    }
}

// =========================================================================
// AD11 · ADVANCED FILTERS — SHEET
// =========================================================================

@Composable
fun AdminAdvancedFilterSheet(
    selectedStatus: String,
    selectedBlock: String,
    priceRangeText: String,
    onStatusChange: (String) -> Unit,
    onBlockChange: (String) -> Unit,
    onPriceRangeChange: (String) -> Unit,
    onApply: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onDismiss() },
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                modifier = Modifier
                    .widthIn(max = 520.dp)
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { /* absorb clicks */ },
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = Color.White,
                shadowElevation = 16.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp, vertical = 18.dp)
                ) {
                    // Top drag handle
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .width(38.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color(0xFFCBD5E1))
                        )
                    }

                    // Title
                    Text(
                        text = "Advanced Filters",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Status Section
                    Text(
                        text = "Status",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF334155)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("All", "Available", "Sold", "On Hold").forEach { statusOption ->
                            val isSelected = selectedStatus.equals(statusOption, ignoreCase = true)
                            Surface(
                                onClick = { onStatusChange(statusOption) },
                                shape = RoundedCornerShape(50),
                                color = if (isSelected) BrandBluePrimary else Color.White,
                                border = if (isSelected) null else BorderStroke(1.dp, Color(0xFFE2E8F0))
                            ) {
                                Box(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = statusOption,
                                        fontSize = 12.5.sp,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else Color(0xFF64748B)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Block Section
                    Text(
                        text = "Block",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF334155)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("All", "A", "B", "C").forEach { blockOption ->
                            val isSelected = selectedBlock.equals(blockOption, ignoreCase = true)
                            Surface(
                                onClick = { onBlockChange(blockOption) },
                                shape = RoundedCornerShape(50),
                                color = if (isSelected) BrandBluePrimary else Color.White,
                                border = if (isSelected) null else BorderStroke(1.dp, Color(0xFFE2E8F0))
                            ) {
                                Box(
                                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 7.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = blockOption,
                                        fontSize = 12.5.sp,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else Color(0xFF64748B)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Price Range Section
                    Text(
                        text = "Price Range (₹)",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF334155)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Price input placeholder box matching mockup AD11
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 13.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (priceRangeText.isBlank()) "Min — Max" else priceRangeText,
                                fontSize = 13.5.sp,
                                color = if (priceRangeText.isBlank()) Color(0xFF94A3B8) else Color(0xFF0F172A),
                                fontWeight = FontWeight.Medium
                            )

                            // Quick preset selector
                            Text(
                                text = "Under ₹60L",
                                fontSize = 11.5.sp,
                                color = BrandBluePrimary,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .clickable { onPriceRangeChange("₹30L — ₹60L") }
                                    .padding(4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(26.dp))

                    // Apply Filters Button
                    Surface(
                        onClick = {
                            onApply()
                            onDismiss()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .shadow(4.dp, shape = RoundedCornerShape(14.dp), spotColor = BrandBluePrimary.copy(alpha = 0.35f)),
                        shape = RoundedCornerShape(14.dp),
                        color = BrandBluePrimary
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "Apply Filters",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

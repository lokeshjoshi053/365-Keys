package com.example.housingroperty.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.housingroperty.ui.theme.*

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    containerColor: Color = BrandBluePrimary,
    contentColor: Color = Color.White
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .shadow(
                elevation = if (enabled) 4.dp else 0.dp,
                shape = RoundedCornerShape(14.dp),
                spotColor = BrandBluePrimary.copy(alpha = 0.35f)
            ),
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = Color(0xFFCBD5E1),
            disabledContentColor = Color.White
        ),
        contentPadding = PaddingValues(vertical = 14.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                color = contentColor,
                strokeWidth = 2.5.dp
            )
        } else {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.2.sp
            )
        }
    }
}

/**
 * Modern tactile Back Arrow Button with circular surface background, border, elevation, and ripple.
 */
@Composable
fun BackArrowButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White,
    borderColor: Color = BorderLight,
    iconColor: Color = TextPrimary,
    size: Dp = 42.dp,
    elevation: Dp = 1.dp,
    shape: androidx.compose.ui.graphics.Shape = CircleShape
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .size(size)
            .shadow(elevation = elevation, shape = shape, spotColor = Color(0x18000000)),
        shape = shape,
        color = containerColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            BackArrowIcon(size = 18.dp, color = iconColor)
        }
    }
}

/**
 * Universal Action Button with matching styling for top bars (e.g. notifications, share, filter).
 */
@Composable
fun ActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White,
    borderColor: Color = BorderLight,
    size: Dp = 42.dp,
    elevation: Dp = 1.dp,
    shape: androidx.compose.ui.graphics.Shape = CircleShape,
    hasBadge: Boolean = false,
    badgeColor: Color = BrandBluePrimary,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        Surface(
            onClick = onClick,
            modifier = Modifier
                .size(size)
                .shadow(elevation = elevation, shape = shape, spotColor = Color(0x18000000)),
            shape = shape,
            color = containerColor,
            border = BorderStroke(1.dp, borderColor)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
        if (hasBadge) {
            Box(
                modifier = Modifier
                    .size(9.dp)
                    .clip(CircleShape)
                    .background(badgeColor)
                    .align(Alignment.TopEnd)
            )
        }
    }
}

/**
 * Unified Top App Bar for all screens.
 * Provides clean alignment, status-bar safe padding, background styling,
 * back button with styled background container, and trailing action slots.
 */
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    onBackClick: (() -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    containerColor: Color = Color.White,
    contentColor: Color = TextPrimary,
    bottomDivider: Boolean = false,
    backButtonContainerColor: Color = Color.White,
    backButtonBorderColor: Color = BorderLight,
    backButtonIconColor: Color = TextPrimary
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = containerColor
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (onBackClick != null) {
                    BackArrowButton(
                        onClick = onBackClick,
                        containerColor = backButtonContainerColor,
                        borderColor = backButtonBorderColor,
                        iconColor = backButtonIconColor
                    )
                    if (title != null) {
                        Spacer(modifier = Modifier.width(14.dp))
                    }
                }

                if (title != null) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = title,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = contentColor,
                            maxLines = 1
                        )
                        if (subtitle != null) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = subtitle,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = TextMuted,
                                maxLines = 1
                            )
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                if (actions != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        actions()
                    }
                }
            }

            if (bottomDivider) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = BorderLight
                )
            }
        }
    }
}

@Composable
fun TopBarBack(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    actions: (@Composable RowScope.() -> Unit)? = null
) {
    AppTopBar(
        onBackClick = onBackClick,
        title = title,
        subtitle = subtitle,
        actions = actions,
        modifier = modifier
    )
}


@Composable
fun PillBadge(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = BrandBlueSoft,
    textColor: Color = BrandBlueDark,
    borderColor: Color? = null
) {
    Box(
        modifier = modifier
            .then(
                if (borderColor != null) Modifier.border(1.dp, borderColor, RoundedCornerShape(50.dp))
                else Modifier
            )
            .background(backgroundColor, RoundedCornerShape(50.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun GoogleSignInButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "Continue with Google"
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        shadowElevation = 0.5.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            GoogleLogoIcon(size = 20.dp)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E293B)
            )
        }
    }
}

/**
 * Blueprint Plot Grid Canvas for Welcome Screen
 * Draws architectural grid lines with subtle plot borders resembling land developments
 */
@Composable
fun BlueprintPlotGrid(
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFF1D58E2),
    gridLineColor: Color = Color(0x33FFFFFF)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Base gradient background
        drawRect(color = primaryColor)

        // Draw isometric/orthogonal plot grid
        val gridSize = 48.dp.toPx()
        val numCols = (w / gridSize).toInt() + 1
        val numRows = (h / gridSize).toInt() + 1

        // Grid lines
        for (i in 0..numCols) {
            val x = i * gridSize
            drawLine(
                color = gridLineColor,
                start = Offset(x, 0f),
                end = Offset(x, h),
                strokeWidth = 1.dp.toPx()
            )
        }
        for (j in 0..numRows) {
            val y = j * gridSize
            drawLine(
                color = gridLineColor,
                start = Offset(0f, y),
                end = Offset(w, y),
                strokeWidth = 1.dp.toPx()
            )
        }

        // Highlight selected plot zones in blueprint style
        val plot1 = Size(gridSize * 2, gridSize * 1.5f)
        drawRect(
            color = Color(0x22FFFFFF),
            topLeft = Offset(gridSize * 1.5f, gridSize * 2f),
            size = plot1
        )
        drawRect(
            color = Color(0x66FFFFFF),
            topLeft = Offset(gridSize * 1.5f, gridSize * 2f),
            size = plot1,
            style = Stroke(width = 1.5.dp.toPx())
        )

        val plot2 = Size(gridSize * 2.5f, gridSize * 2f)
        drawRect(
            color = Color(0x28FFFFFF),
            topLeft = Offset(gridSize * 4.2f, gridSize * 2.5f),
            size = plot2
        )
        drawRect(
            color = Color(0x66FFFFFF),
            topLeft = Offset(gridSize * 4.2f, gridSize * 2.5f),
            size = plot2,
            style = Stroke(width = 1.5.dp.toPx())
        )
    }
}

// ==========================================
// DIALOGS (Mockup M1, M2, M3, M4)
// ==========================================

@Composable
fun AppDialog(
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
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
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .widthIn(max = 420.dp)
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { /* Consume click inside card */ },
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = content
                )
            }
        }
    }
}

/**
 * M1 · Confirm Logout Dialog
 */
@Composable
fun LogoutConfirmDialog(
    onDismiss: () -> Unit,
    onConfirmLogout: () -> Unit
) {
    AppDialog(onDismiss = onDismiss) {
        LogoutDialogIcon(size = 34.dp, color = Color(0xFF0F172A))
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Log out?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "You'll need to sign in again to access your bookings and documents.",
            fontSize = 13.5.sp,
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center,
            lineHeight = 19.sp
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
                color = Color(0xFFF1F5F9)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Cancel",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BrandBluePrimary
                    )
                }
            }
            Surface(
                onClick = onConfirmLogout,
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFDC2626)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Log Out",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

/**
 * Exit App Confirmation Dialog
 * Displayed when user requests to exit from the home screen
 */
@Composable
fun ExitAppConfirmDialog(
    onDismiss: () -> Unit,
    onConfirmExit: () -> Unit
) {
    AppDialog(onDismiss = onDismiss) {
        LogoutDialogIcon(size = 36.dp, color = Color(0xFF0F172A))
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Are you sure you want to exit?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Do you want to close the application? You can reopen anytime.",
            fontSize = 13.5.sp,
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center,
            lineHeight = 19.sp
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
                color = Color(0xFFF1F5F9)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Cancel",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BrandBluePrimary
                    )
                }
            }
            Surface(
                onClick = onConfirmExit,
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFDC2626)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Exit",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

/**
 * M2 · Payment Failed Dialog
 */
@Composable
fun PaymentFailedDialog(
    onDismiss: () -> Unit,
    onRetry: () -> Unit
) {
    AppDialog(onDismiss = onDismiss) {
        ErrorCircleIcon(size = 36.dp, color = Color(0xFF0F172A))
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Payment Failed",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Your bank declined the transaction. No amount has been deducted.",
            fontSize = 13.5.sp,
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center,
            lineHeight = 19.sp
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
                color = Color(0xFFF1F5F9)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Cancel",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BrandBluePrimary
                    )
                }
            }
            Surface(
                onClick = onRetry,
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp),
                shape = RoundedCornerShape(12.dp),
                color = BrandBluePrimary
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Try Again",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

/**
 * M3 · Plot Unavailable Dialog
 */
@Composable
fun PlotUnavailableDialog(
    plotName: String = "Plot A-114",
    onDismiss: () -> Unit,
    onBrowseOther: () -> Unit
) {
    AppDialog(onDismiss = onDismiss) {
        ProhibitedCircleIcon(size = 36.dp, color = Color(0xFF0F172A))
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Plot No Longer Available",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$plotName was just booked by another customer. Please choose another plot.",
            fontSize = 13.5.sp,
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center,
            lineHeight = 19.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Surface(
            onClick = onBrowseOther,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            color = BrandBluePrimary
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "Browse Other Plots",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

/**
 * M4 · Session Expired Dialog
 */
@Composable
fun SessionExpiredDialog(
    onDismiss: () -> Unit = {},
    onLogIn: () -> Unit
) {
    AppDialog(onDismiss = onDismiss) {
        LockIcon(size = 34.dp, color = Color(0xFF0F172A))
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Session Expired",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "For your security, please log in again to continue.",
            fontSize = 13.5.sp,
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center,
            lineHeight = 19.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Surface(
            onClick = onLogIn,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            color = BrandBluePrimary
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "Log In",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

// ==========================================
// BOTTOM SHEETS (Mockup B1 & B2)
// ==========================================

@Composable
fun AppBottomSheet(
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.45f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onDismiss() }
        ) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { /* Consume */ },
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = Color.White,
                shadowElevation = 16.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, top = 14.dp, bottom = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Drag Handle
                    Box(
                        modifier = Modifier
                            .width(38.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFFCBD5E1))
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    content()
                }
            }
        }
    }
}

/**
 * B1 · Filter Plots Bottom Sheet
 */
@Composable
fun FilterPlotsBottomSheet(
    selectedBlock: String = "All",
    selectedStatus: String = "Available",
    onDismiss: () -> Unit,
    onApply: (block: String, status: String) -> Unit
) {
    var curBlock by remember { mutableStateOf(selectedBlock) }
    var curStatus by remember { mutableStateOf(selectedStatus) }

    val blockOptions = listOf("All", "A", "B", "C")
    val statusOptions = listOf("Available", "Sold")

    AppBottomSheet(onDismiss = onDismiss) {
        Text(
            text = "Filter Plots",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(22.dp))

        // Block Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Block",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF475569),
                modifier = Modifier.width(60.dp)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                blockOptions.forEach { block ->
                    val isSelected = curBlock == block
                    Surface(
                        onClick = { curBlock = block },
                        shape = RoundedCornerShape(50),
                        color = if (isSelected) BrandBluePrimary else Color.White,
                        border = if (isSelected) null else BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Box(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = block,
                                fontSize = 13.5.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Color(0xFF475569)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Status Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Status",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF475569),
                modifier = Modifier.width(60.dp)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                statusOptions.forEach { status ->
                    val isSelected = curStatus == status
                    Surface(
                        onClick = { curStatus = status },
                        shape = RoundedCornerShape(50),
                        color = if (isSelected) BrandBluePrimary else Color.White,
                        border = if (isSelected) null else BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Box(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = status,
                                fontSize = 13.5.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Color(0xFF475569)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Apply Button
        Surface(
            onClick = { onApply(curBlock, curStatus) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            color = BrandBluePrimary
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "Apply Filters",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

/**
 * B2 · E-sign Booking Agreement Bottom Sheet
 */
@Composable
fun SignAgreementBottomSheet(
    onDismiss: () -> Unit,
    onSignNow: () -> Unit
) {
    var isAgreed by remember { mutableStateOf(false) }

    AppBottomSheet(onDismiss = onDismiss) {
        Text(
            text = "Sign Booking Agreement",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isAgreed = !isAgreed }
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isAgreed,
                onCheckedChange = { isAgreed = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = BrandBluePrimary,
                    uncheckedColor = Color(0xFF94A3B8)
                )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "I have reviewed and agree to the terms of this agreement.",
                fontSize = 13.5.sp,
                color = Color(0xFF475569),
                lineHeight = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            onClick = { if (isAgreed) onSignNow() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            color = if (isAgreed) BrandBluePrimary else Color(0xFFCBD5E1)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "Sign Now",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

// ==========================================
// TOAST BANNERS (Mockup T1 & T2)
// ==========================================

enum class ToastType {
    SUCCESS,
    ERROR
}

/**
 * T1 & T2 · Top Floating Toast Banner
 */
@Composable
fun AppToastBanner(
    message: String,
    type: ToastType,
    modifier: Modifier = Modifier,
    onDismiss: (() -> Unit)? = null
) {
    val backgroundColor = when (type) {
        ToastType.SUCCESS -> Color(0xFF16A34A) // Green
        ToastType.ERROR -> Color(0xFFDC2626)   // Red
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(14.dp), spotColor = Color(0x33000000)),
        shape = RoundedCornerShape(14.dp),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (type) {
                ToastType.SUCCESS -> {
                    CheckmarkIcon(size = 18.dp, color = Color.White)
                }
                ToastType.ERROR -> {
                    WarningTriangleIcon(size = 18.dp, color = Color.White)
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = message,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            if (onDismiss != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "✕",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.clickable { onDismiss() }
                )
            }
        }
    }
}

// ==========================================
// LOADING, EMPTY & ERROR STATES (Mockup S1, S2, S3)
// ==========================================

/**
 * S1 · Loading Skeleton Cards
 */
@Composable
fun SkeletonLoadingView(
    modifier: Modifier = Modifier,
    count: Int = 4
) {
    val transition = rememberInfiniteTransition()
    val alpha by transition.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        repeat(count) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(86.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE2E8F0).copy(alpha = alpha))
            )
        }
    }
}

/**
 * S2 · Empty State View
 */
@Composable
fun EmptyStateView(
    title: String = "You're all caught up",
    subtitle: String = "New booking, payment and document updates will show up here.",
    icon: @Composable () -> Unit = { BellOutlineIcon(size = 46.dp, color = Color(0xFF1E293B)) },
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            icon()
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
                Spacer(modifier = Modifier.height(24.dp))
                Surface(
                    onClick = onActionClick,
                    shape = RoundedCornerShape(50),
                    color = Color(0xFFEFF6FF),
                    border = BorderStroke(1.dp, Color(0xFFBFDBFE))
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 28.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = actionText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BrandBluePrimary
                        )
                    }
                }
            }
        }
    }
}

/**
 * S3 · Error / Retry State View
 */
@Composable
fun ErrorStateView(
    title: String = "Couldn't load plots",
    subtitle: String = "Check your internet connection and try again.",
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WarningTriangleIcon(size = 46.dp, color = Color(0xFF1E293B))
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
            Spacer(modifier = Modifier.height(22.dp))
            Surface(
                onClick = onRetry,
                shape = RoundedCornerShape(50),
                color = Color(0xFFF1F5F9),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Retry",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BrandBluePrimary
                    )
                }
            }
        }
    }
}

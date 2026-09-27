package com.example.housingroperty.ui.customer.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import com.example.housingroperty.data.models.Plot
import com.example.housingroperty.data.models.PlotStatus
import com.example.housingroperty.permissions.PermissionRationaleDialog
import com.example.housingroperty.permissions.PermissionStatus
import com.example.housingroperty.permissions.PermissionType
import com.example.housingroperty.permissions.rememberPermissionController
import com.example.housingroperty.ui.components.AppTopBar
import com.example.housingroperty.ui.components.PrimaryButton
import com.example.housingroperty.ui.customer.components.MapPinIcon
import com.example.housingroperty.ui.customer.components.StatusBadge
import com.example.housingroperty.ui.theme.BrandBluePrimary
import com.example.housingroperty.ui.theme.TextPrimary
import com.example.housingroperty.ui.theme.TextSecondary

enum class MapLayerMode {
    BLUEPRINT,
    SATELLITE
}

@Composable
fun PlotMapScreen(
    plots: List<Plot>,
    initialSelectedPlot: Plot? = null,
    onBack: () -> Unit,
    onSelectPlan: (Plot) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedPlot by remember { mutableStateOf(initialSelectedPlot ?: plots.firstOrNull()) }
    var layerMode by remember { mutableStateOf(MapLayerMode.BLUEPRINT) }

    val permissionController = rememberPermissionController()
    var showLocationRationale by remember { mutableStateOf(false) }
    var hasLocationPermission by remember {
        mutableStateOf(permissionController.getPermissionStatus(PermissionType.LOCATION) == PermissionStatus.GRANTED)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .safeDrawingPadding()
    ) {
        // App Top Bar with Layer Switcher
        AppTopBar(
            title = "Master Township Plan",
            subtitle = "Bhopal Ring Road Sector 4 · ${plots.size} Plots",
            onBackClick = onBack,
            actions = {
                // Layer Mode Switcher Pill
                Surface(
                    onClick = {
                        layerMode = if (layerMode == MapLayerMode.BLUEPRINT) MapLayerMode.SATELLITE else MapLayerMode.BLUEPRINT
                    },
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFF1F5F9),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = if (layerMode == MapLayerMode.BLUEPRINT) "🗺️ Blueprint" else "🛰️ Satellite",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandBluePrimary
                        )
                    }
                }
            }
        )

        // Interactive Map View Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            InteractivePlotMapView(
                plots = plots,
                selectedPlot = selectedPlot,
                onPlotSelected = { plot -> selectedPlot = plot },
                layerMode = layerMode,
                hasLocationPermission = hasLocationPermission,
                onRequestLocationPermission = {
                    val status = permissionController.getPermissionStatus(PermissionType.LOCATION)
                    if (status != PermissionStatus.GRANTED) {
                        showLocationRationale = true
                    } else {
                        hasLocationPermission = true
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // Bottom Selected Plot Card
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = selectedPlot != null,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                ) {
                    selectedPlot?.let { plot ->
                        PlotMapBottomCard(
                            plot = plot,
                            onClose = { selectedPlot = null },
                            onGetDirections = {
                                permissionController.openExternalMap(
                                    latitude = plot.latitude,
                                    longitude = plot.longitude,
                                    label = "${plot.title} (${plot.block})"
                                )
                            },
                            onSelectPlan = { onSelectPlan(plot) }
                        )
                    }
                }
            }
        }
    }

    if (showLocationRationale) {
        PermissionRationaleDialog(
            permissionType = PermissionType.LOCATION,
            controller = permissionController,
            onDismiss = { showLocationRationale = false },
            onPermissionResult = { granted ->
                showLocationRationale = false
                if (granted) {
                    hasLocationPermission = true
                }
            }
        )
    }
}

@Composable
fun InteractivePlotMapView(
    plots: List<Plot>,
    selectedPlot: Plot?,
    onPlotSelected: (Plot) -> Unit,
    layerMode: MapLayerMode,
    hasLocationPermission: Boolean,
    onRequestLocationPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    // When a plot is selected or changes, center the map roughly on it
    LaunchedEffect(selectedPlot) {
        selectedPlot?.let { p ->
            // Center viewport on the selected plot offset
            offsetX = (0.5f - p.mapOffsetX) * 600f
            offsetY = (0.5f - p.mapOffsetY) * 600f
            scale = 1.35f
        }
    }

    Box(
        modifier = modifier
            .clipToBounds()
            .background(if (layerMode == MapLayerMode.BLUEPRINT) Color(0xFF0F1E36) else Color(0xFF1E2F1F))
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(0.6f, 3.5f)
                    offsetX += pan.x
                    offsetY += pan.y
                }
            }
    ) {
        // Map Canvas Drawing
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasW = size.width
            val canvasH = size.height

            // Center origin
            val centerX = canvasW / 2f + offsetX
            val centerY = canvasH / 2f + offsetY

            // Map layout dimensions
            val mapBaseWidth = canvasW.coerceAtLeast(360f) * 1.5f * scale
            val mapBaseHeight = canvasH.coerceAtLeast(500f) * 1.5f * scale

            val left = centerX - mapBaseWidth / 2f
            val top = centerY - mapBaseHeight / 2f

            drawTownshipLayout(
                left = left,
                top = top,
                width = mapBaseWidth,
                height = mapBaseHeight,
                layerMode = layerMode,
                hasLocation = hasLocationPermission
            )

            // Draw Plots
            plots.forEach { plot ->
                val plotX = left + mapBaseWidth * plot.mapOffsetX
                val plotY = top + mapBaseHeight * plot.mapOffsetY
                val isSelected = plot.id == selectedPlot?.id

                drawPlotParcel(
                    plot = plot,
                    centerX = plotX,
                    centerY = plotY,
                    scale = scale,
                    isSelected = isSelected,
                    layerMode = layerMode
                )
            }
        }

        // Overlay Plot Touch Anchors for accurate tapping
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val canvasW = constraints.maxWidth.toFloat()
            val canvasH = constraints.maxHeight.toFloat()
            val centerX = canvasW / 2f + offsetX
            val centerY = canvasH / 2f + offsetY
            val mapBaseWidth = canvasW.coerceAtLeast(360f) * 1.5f * scale
            val mapBaseHeight = canvasH.coerceAtLeast(500f) * 1.5f * scale
            val left = centerX - mapBaseWidth / 2f
            val top = centerY - mapBaseHeight / 2f

            val density = androidx.compose.ui.platform.LocalDensity.current

            plots.forEach { plot ->
                val plotX = left + mapBaseWidth * plot.mapOffsetX
                val plotY = top + mapBaseHeight * plot.mapOffsetY
                val isSelected = plot.id == selectedPlot?.id
                val hitSizePx = (48f * scale).coerceIn(36f, 72f)
                val hitSizeDp = with(density) { hitSizePx.toDp() }

                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                x = (plotX - hitSizePx / 2f).roundToInt(),
                                y = (plotY - hitSizePx / 2f).roundToInt()
                            )
                        }
                        .size(hitSizeDp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onPlotSelected(plot) }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0x333B82F6),
                            border = BorderStroke(2.dp, Color(0xFF60A5FA)),
                            modifier = Modifier.size(hitSizeDp + 8.dp)
                        ) {}
                    }
                }
            }
        }

        // Top Status & GPS Pill
        Surface(
            onClick = onRequestLocationPermission,
            shape = RoundedCornerShape(50),
            color = if (hasLocationPermission) Color(0xEE14532D) else Color(0xEE1E293B),
            border = BorderStroke(
                1.dp,
                if (hasLocationPermission) Color(0xFF22C55E) else Color(0xFF475569)
            ),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                MapPinIcon(
                    size = 13.dp,
                    color = if (hasLocationPermission) Color(0xFF4ADE80) else Color(0xFF94A3B8)
                )
                Text(
                    text = if (hasLocationPermission) "GPS Active · Near Bhopal Ring Rd" else "Enable GPS for live directions",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (hasLocationPermission) Color(0xFF86EFAC) else Color(0xFFCBD5E1)
                )
            }
        }

        // Map Zoom Controls & Legend
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Zoom In Button
            Surface(
                onClick = { scale = (scale * 1.25f).coerceAtMost(3.5f) },
                shape = RoundedCornerShape(10.dp),
                color = Color(0xDD0F172A),
                border = BorderStroke(1.dp, Color(0xFF334155)),
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("+", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Zoom Out Button
            Surface(
                onClick = { scale = (scale / 1.25f).coerceAtLeast(0.6f) },
                shape = RoundedCornerShape(10.dp),
                color = Color(0xDD0F172A),
                border = BorderStroke(1.dp, Color(0xFF334155)),
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("−", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Reset View Button
            Surface(
                onClick = {
                    scale = 1f
                    offsetX = 0f
                    offsetY = 0f
                },
                shape = RoundedCornerShape(10.dp),
                color = Color(0xDD0F172A),
                border = BorderStroke(1.dp, Color(0xFF334155)),
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("⟲", color = Color(0xFF94A3B8), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Legend at bottom left (when card is collapsed)
        if (selectedPlot == null) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xEE0F172A),
                border = BorderStroke(1.dp, Color(0xFF334155)),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("LAYOUT LEGEND", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF10B981)))
                        Text("Available", fontSize = 10.sp, color = Color.White)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFF59E0B)))
                        Text("Booked", fontSize = 10.sp, color = Color.White)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF64748B)))
                        Text("Sold", fontSize = 10.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawTownshipLayout(
    left: Float,
    top: Float,
    width: Float,
    height: Float,
    layerMode: MapLayerMode,
    hasLocation: Boolean
) {
    val isBlueprint = layerMode == MapLayerMode.BLUEPRINT

    // Background township plot
    val bgColor = if (isBlueprint) Color(0xFF0F1E36) else Color(0xFF1B2E1E)
    drawRoundRect(
        color = bgColor,
        topLeft = Offset(left, top),
        size = Size(width, height),
        cornerRadius = CornerRadius(24f, 24f)
    )

    // Outer boundary fence / wall
    drawRoundRect(
        color = if (isBlueprint) Color(0xFF2563EB) else Color(0xFF4ADE80),
        topLeft = Offset(left, top),
        size = Size(width, height),
        cornerRadius = CornerRadius(24f, 24f),
        style = Stroke(
            width = 4f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
        )
    )

    // Blueprint grid lines
    if (isBlueprint) {
        val gridStep = 40f
        var gx = left
        while (gx < left + width) {
            drawLine(
                color = Color(0x1538BDF8),
                start = Offset(gx, top),
                end = Offset(gx, top + height),
                strokeWidth = 1f
            )
            gx += gridStep
        }
        var gy = top
        while (gy < top + height) {
            drawLine(
                color = Color(0x1538BDF8),
                start = Offset(left, gy),
                end = Offset(left + width, gy),
                strokeWidth = 1f
            )
            gy += gridStep
        }
    } else {
        // Satellite park / greens
        drawRoundRect(
            color = Color(0xFF14532D),
            topLeft = Offset(left + width * 0.45f, top + height * 0.42f),
            size = Size(width * 0.16f, height * 0.16f),
            cornerRadius = CornerRadius(16f, 16f)
        )
        // Clubhouse pool
        drawRoundRect(
            color = Color(0xFF0284C7),
            topLeft = Offset(left + width * 0.49f, top + height * 0.47f),
            size = Size(width * 0.08f, height * 0.06f),
            cornerRadius = CornerRadius(8f, 8f)
        )
    }

    // Roads
    val roadColor = if (isBlueprint) Color(0xFF1E293B) else Color(0xFF334155)
    val dividerColor = if (isBlueprint) Color(0x6638BDF8) else Color(0x88FACC15)

    // Main 60ft Boulevard (Horizontal center)
    val hRoadY = top + height * 0.5f
    val hRoadHeight = height * 0.08f
    drawRect(
        color = roadColor,
        topLeft = Offset(left, hRoadY - hRoadHeight / 2f),
        size = Size(width, hRoadHeight)
    )
    // Divider line
    drawLine(
        color = dividerColor,
        start = Offset(left, hRoadY),
        end = Offset(left + width, hRoadY),
        strokeWidth = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 12f), 0f)
    )

    // Vertical Main Sector Street (Center)
    val vRoadX = left + width * 0.5f
    val vRoadWidth = width * 0.08f
    drawRect(
        color = roadColor,
        topLeft = Offset(vRoadX - vRoadWidth / 2f, top),
        size = Size(vRoadWidth, height)
    )
    // Divider line
    drawLine(
        color = dividerColor,
        start = Offset(vRoadX, top),
        end = Offset(vRoadX, top + height),
        strokeWidth = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 12f), 0f)
    )

    // Sector secondary streets
    val streetColor = if (isBlueprint) Color(0xFF172554) else Color(0xFF24303E)
    // North Sector street
    drawRect(
        color = streetColor,
        topLeft = Offset(left, top + height * 0.22f),
        size = Size(width, height * 0.035f)
    )
    // South Sector street
    drawRect(
        color = streetColor,
        topLeft = Offset(left, top + height * 0.78f),
        size = Size(width, height * 0.035f)
    )

    // Entrance Gate
    val gateWidth = width * 0.12f
    val gateHeight = 12f
    drawRoundRect(
        color = Color(0xFFE2E8F0),
        topLeft = Offset(left - gateHeight, hRoadY - gateWidth / 2f),
        size = Size(gateHeight * 2f, gateWidth),
        cornerRadius = CornerRadius(4f, 4f)
    )

    // User Live GPS pin near Entrance Gate if permission active
    if (hasLocation) {
        val userX = left + width * 0.08f
        val userY = hRoadY
        // Pulse ring
        drawCircle(
            color = Color(0x443B82F6),
            radius = 24f,
            center = Offset(userX, userY)
        )
        // Solid GPS dot
        drawCircle(
            color = Color(0xFF3B82F6),
            radius = 8f,
            center = Offset(userX, userY)
        )
        drawCircle(
            color = Color.White,
            radius = 3.5f,
            center = Offset(userX, userY)
        )
    }
}

private fun DrawScope.drawPlotParcel(
    plot: Plot,
    centerX: Float,
    centerY: Float,
    scale: Float,
    isSelected: Boolean,
    layerMode: MapLayerMode
) {
    val parcelW = (64f * scale).coerceIn(44f, 100f)
    val parcelH = (48f * scale).coerceIn(34f, 80f)

    val fillColor = when (plot.status) {
        PlotStatus.AVAILABLE -> if (isSelected) Color(0xFF059669) else Color(0x3310B981)
        PlotStatus.BOOKED -> if (isSelected) Color(0xFFD97706) else Color(0x33F59E0B)
        PlotStatus.SOLD -> if (isSelected) Color(0xFF475569) else Color(0x2264748B)
        PlotStatus.ON_HOLD -> if (isSelected) Color(0xFF7C3AED) else Color(0x338B5CF6)
    }

    val strokeColor = when (plot.status) {
        PlotStatus.AVAILABLE -> Color(0xFF10B981)
        PlotStatus.BOOKED -> Color(0xFFF59E0B)
        PlotStatus.SOLD -> Color(0xFF64748B)
        PlotStatus.ON_HOLD -> Color(0xFF8B5CF6)
    }

    // Parcel background
    drawRoundRect(
        color = fillColor,
        topLeft = Offset(centerX - parcelW / 2f, centerY - parcelH / 2f),
        size = Size(parcelW, parcelH),
        cornerRadius = CornerRadius(6f * scale, 6f * scale)
    )

    // Parcel border
    drawRoundRect(
        color = if (isSelected) Color.White else strokeColor,
        topLeft = Offset(centerX - parcelW / 2f, centerY - parcelH / 2f),
        size = Size(parcelW, parcelH),
        cornerRadius = CornerRadius(6f * scale, 6f * scale),
        style = Stroke(width = if (isSelected) 3f else 1.5f)
    )

    // Plot Pin on top of parcel
    val pinRadius = (10f * scale).coerceIn(7f, 15f)
    drawCircle(
        color = strokeColor,
        radius = pinRadius,
        center = Offset(centerX, centerY)
    )
    drawCircle(
        color = if (isSelected) Color.White else Color(0xFF0F172A),
        radius = pinRadius * 0.5f,
        center = Offset(centerX, centerY)
    )
}

@Composable
private fun PlotMapBottomCard(
    plot: Plot,
    onClose: () -> Unit,
    onGetDirections: () -> Unit,
    onSelectPlan: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 16.dp, shape = RoundedCornerShape(22.dp)),
        shape = RoundedCornerShape(22.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Header Row: Title + Block + Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = plot.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        StatusBadge(label = plot.status.label)
                    }
                    Text(
                        text = "${plot.block} · ${plot.location}",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                }

                // Close Button
                Surface(
                    onClick = onClose,
                    shape = CircleShape,
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("✕", fontSize = 13.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Specs Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF8FAFC))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Area", fontSize = 11.sp, color = TextSecondary)
                    Text(plot.sizeSqFt, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
                Column {
                    Text("Facing", fontSize = 11.sp, color = TextSecondary)
                    Text(plot.facing, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
                Column {
                    Text("Road", fontSize = 11.sp, color = TextSecondary)
                    Text(plot.roadWidth, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
                Column {
                    Text("Total Price", fontSize = 11.sp, color = TextSecondary)
                    Text(plot.totalPriceFormatted, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BrandBluePrimary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Directions in Maps
                Surface(
                    onClick = onGetDirections,
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFEFF6FF),
                    border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MapPinIcon(size = 16.dp, color = BrandBluePrimary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Directions",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandBluePrimary
                        )
                    }
                }

                // Select Plan / Book Now
                if (plot.isAvailable) {
                    Surface(
                        onClick = onSelectPlan,
                        shape = RoundedCornerShape(12.dp),
                        color = BrandBluePrimary,
                        modifier = Modifier
                            .weight(1.3f)
                            .height(48.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Select Plan",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

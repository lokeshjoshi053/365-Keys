package com.example.housingroperty.ui.customer.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.housingroperty.data.models.Plot
import com.example.housingroperty.data.models.PlotStatus
import com.example.housingroperty.ui.components.BlueprintPlotGrid
import com.example.housingroperty.ui.components.EmptyStateView
import com.example.housingroperty.ui.components.ErrorStateView
import com.example.housingroperty.ui.components.FilterPlotsBottomSheet
import com.example.housingroperty.ui.components.PlotUnavailableDialog
import com.example.housingroperty.ui.components.SkeletonLoadingView
import com.example.housingroperty.ui.customer.components.FilterIcon
import com.example.housingroperty.ui.customer.components.MapPinIcon
import com.example.housingroperty.ui.customer.components.StatusBadge
import com.example.housingroperty.ui.theme.*

@Composable
fun PlotListScreen(
    plots: List<Plot>,
    onPlotClick: (Plot) -> Unit,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var isMapView by remember { mutableStateOf(false) }
    var selectedMapPlot by remember { mutableStateOf<Plot?>(null) }
    var mapLayerMode by remember { mutableStateOf(MapLayerMode.BLUEPRINT) }
    var showFilterSheet by remember { mutableStateOf(false) }
    var filterBlock by remember { mutableStateOf("All") }
    var filterStatus by remember { mutableStateOf("All") }
    var unavailablePlot by remember { mutableStateOf<Plot?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var hasError by remember { mutableStateOf(false) }

    val permissionController = com.example.housingroperty.permissions.rememberPermissionController()
    var showLocationRationale by remember { mutableStateOf(false) }
    var hasLocationPermission by remember {
        mutableStateOf(permissionController.getPermissionStatus(com.example.housingroperty.permissions.PermissionType.LOCATION) == com.example.housingroperty.permissions.PermissionStatus.GRANTED)
    }

    val filteredPlots = remember(plots, filterBlock, filterStatus) {
        plots.filter { plot ->
            val matchBlock = filterBlock == "All" || plot.block.contains(filterBlock, ignoreCase = true)
            val matchStatus = filterStatus == "All" || plot.status.label.equals(filterStatus, ignoreCase = true)
            matchBlock && matchStatus
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {
        // Top Header
        com.example.housingroperty.ui.components.AppTopBar(
            title = "Plots Inventory",
            subtitle = "${filteredPlots.size} verified properties",
            onBackClick = onBack,
            actions = {
                com.example.housingroperty.ui.components.ActionButton(
                    onClick = { showFilterSheet = true }
                ) {
                    FilterIcon(size = 20.dp, color = TextPrimary)
                }
            }
        )

        // Toggle Row: "List / Map toggle" + Layer Switcher (when in map mode)
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 6.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                onClick = {
                    if (!isMapView) {
                        val status = permissionController.getPermissionStatus(com.example.housingroperty.permissions.PermissionType.LOCATION)
                        if (status != com.example.housingroperty.permissions.PermissionStatus.GRANTED) {
                            showLocationRationale = true
                        } else {
                            hasLocationPermission = true
                        }
                        isMapView = true
                    } else {
                        isMapView = false
                    }
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(50.dp),
                color = Color(0xFFF1F5F9),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Text(
                    text = if (isMapView) "🗺️ Map View · Tap for List" else "📑 List View · Tap for Map (${plots.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }

            if (isMapView) {
                Surface(
                    onClick = {
                        mapLayerMode = if (mapLayerMode == MapLayerMode.BLUEPRINT) MapLayerMode.SATELLITE else MapLayerMode.BLUEPRINT
                    },
                    shape = RoundedCornerShape(50.dp),
                    color = Color(0xFFEFF6FF),
                    border = BorderStroke(1.dp, Color(0xFFBFDBFE))
                ) {
                    Text(
                        text = if (mapLayerMode == MapLayerMode.BLUEPRINT) "Blueprint" else "Satellite",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandBluePrimary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when {
            hasError -> {
                ErrorStateView(
                    title = "Couldn't load plots",
                    subtitle = "Check your internet connection and try again.",
                    onRetry = {
                        hasError = false
                        isLoading = false
                    }
                )
            }
            isLoading -> {
                SkeletonLoadingView(count = 4)
            }
            isMapView -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    InteractivePlotMapView(
                        plots = filteredPlots,
                        selectedPlot = selectedMapPlot,
                        onPlotSelected = { plot -> selectedMapPlot = plot },
                        layerMode = mapLayerMode,
                        hasLocationPermission = hasLocationPermission,
                        onRequestLocationPermission = {
                            if (!hasLocationPermission) {
                                showLocationRationale = true
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    // Floating Selected Plot Card
                    selectedMapPlot?.let { plot ->
                        Surface(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(12.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = Color.White,
                            shadowElevation = 10.dp
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
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
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary
                                            )
                                            StatusBadge(label = plot.status.label)
                                        }
                                        Text(
                                            text = "${plot.block} · ${plot.sizeSqFt} · ${plot.roadWidth}",
                                            fontSize = 12.sp,
                                            color = TextSecondary
                                        )
                                    }
                                    Text(
                                        text = plot.totalPriceFormatted,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BrandBluePrimary
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Surface(
                                        onClick = {
                                            permissionController.openExternalMap(
                                                latitude = plot.latitude,
                                                longitude = plot.longitude,
                                                label = "${plot.title} (${plot.block})"
                                            )
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFFEFF6FF),
                                        border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(42.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxSize(),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            MapPinIcon(size = 14.dp, color = BrandBluePrimary)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "Directions",
                                                fontSize = 12.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = BrandBluePrimary
                                            )
                                        }
                                    }

                                    Surface(
                                        onClick = {
                                            if (plot.status == PlotStatus.SOLD) {
                                                unavailablePlot = plot
                                            } else {
                                                onPlotClick(plot)
                                            }
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        color = BrandBluePrimary,
                                        modifier = Modifier
                                            .weight(1.2f)
                                            .height(42.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "View Details",
                                                fontSize = 12.5.sp,
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
            }
            filteredPlots.isEmpty() -> {
                EmptyStateView(
                    title = "No plots found",
                    subtitle = "Try adjusting your filter criteria to see available inventory.",
                    actionText = "Reset Filters",
                    onActionClick = {
                        filterBlock = "All"
                        filterStatus = "All"
                    }
                )
            }
            else -> {
                // Plot List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredPlots) { plot ->
                        PlotCard(
                            plot = plot,
                            onClick = {
                                if (plot.status == PlotStatus.SOLD) {
                                    unavailablePlot = plot
                                } else {
                                    onPlotClick(plot)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    // Filter Bottom Sheet (Mockup B1)
    if (showFilterSheet) {
        FilterPlotsBottomSheet(
            selectedBlock = filterBlock,
            selectedStatus = filterStatus,
            onDismiss = { showFilterSheet = false },
            onApply = { block, status ->
                filterBlock = block
                filterStatus = status
                showFilterSheet = false
            }
        )
    }

    // Unavailable Plot Dialog (Mockup M3)
    unavailablePlot?.let { plot ->
        PlotUnavailableDialog(
            plotName = plot.title,
            onDismiss = { unavailablePlot = null },
            onBrowseOther = {
                unavailablePlot = null
                filterStatus = "Available"
            }
        )
    }

    // Location Permission Rationale Dialog
    if (showLocationRationale) {
        com.example.housingroperty.permissions.PermissionRationaleDialog(
            permissionType = com.example.housingroperty.permissions.PermissionType.LOCATION,
            controller = permissionController,
            onDismiss = { showLocationRationale = false },
            onPermissionResult = { isGranted ->
                hasLocationPermission = isGranted
                showLocationRationale = false
            }
        )
    }
}

@Composable
private fun PlotCard(
    plot: Plot,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pin Icon Container
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

            Column(modifier = Modifier.weight(1f)) {
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

            StatusBadge(label = plot.status.label)
        }
    }
}

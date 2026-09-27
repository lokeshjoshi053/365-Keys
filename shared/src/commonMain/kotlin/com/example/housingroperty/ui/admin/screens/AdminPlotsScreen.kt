package com.example.housingroperty.ui.admin.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.housingroperty.data.models.Plot
import com.example.housingroperty.data.models.PlotStatus
import com.example.housingroperty.ui.admin.components.*
import com.example.housingroperty.ui.components.*
import com.example.housingroperty.ui.customer.components.MapPinIcon
import com.example.housingroperty.ui.customer.components.StatusBadge
import com.example.housingroperty.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun AdminPlotsScreen(
    plots: List<Plot>,
    onTogglePlotStatus: (Plot) -> Unit,
    onDeletePlot: ((Plot) -> Unit)? = null,
    onAddPlot: ((Plot) -> Unit)? = null,
    onUpdatePrice: ((Plot, String) -> Unit)? = null,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    // Search & Filter State
    var searchQuery by remember { mutableStateOf("") }
    var filterBlock by remember { mutableStateOf("All") }
    var filterStatus by remember { mutableStateOf("All") }
    var priceRangeText by remember { mutableStateOf("") }
    var showFilterSheet by remember { mutableStateOf(false) }

    // Dialog & Feedback States
    var plotToDelete by remember { mutableStateOf<Plot?>(null) }
    var plotToUpdatePrice by remember { mutableStateOf<Plot?>(null) }
    var plotToToggleStatus by remember { mutableStateOf<Plot?>(null) }
    var toastMessage by remember { mutableStateOf<String?>(null) }

    // Loading & Demo States (AD13 & AD14)
    var isSimulatingLoading by remember { mutableStateOf(false) }
    var isUserModeDemo by remember { mutableStateOf(false) }

    // Auto-dismiss toast
    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            delay(3500)
            toastMessage = null
        }
    }

    val filteredPlots = remember(plots, filterBlock, filterStatus, priceRangeText, searchQuery, isUserModeDemo) {
        if (isUserModeDemo) {
            emptyList()
        } else {
            plots.filter { plot ->
                val matchBlock = filterBlock == "All" || plot.block.contains(filterBlock, ignoreCase = true)
                val matchStatus = filterStatus == "All" || plot.status.label.equals(filterStatus, ignoreCase = true)
                val matchPrice = priceRangeText.isBlank() || plot.totalPriceFormatted.contains(priceRangeText, ignoreCase = true) || plot.pricePerSqFt.contains(priceRangeText, ignoreCase = true)
                val matchQuery = searchQuery.isBlank() ||
                        plot.title.contains(searchQuery, ignoreCase = true) ||
                        plot.block.contains(searchQuery, ignoreCase = true) ||
                        plot.location.contains(searchQuery, ignoreCase = true)
                matchBlock && matchStatus && matchPrice && matchQuery
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar matching mockup "Plot Management" / "User Management"
            AppTopBar(
                title = if (isUserModeDemo) "User Management" else "Plot Management",
                subtitle = if (isUserModeDemo) "0 users matching query" else "${filteredPlots.size} of ${plots.size} plots",
                onBackClick = onBack,
                actions = {
                    // Demo toggle button to simulate AD13 Loading skeleton
                    Surface(
                        onClick = { isSimulatingLoading = !isSimulatingLoading },
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSimulatingLoading) Color(0xFFEFF6FF) else Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, if (isSimulatingLoading) BrandBluePrimary else Color(0xFFE2E8F0))
                    ) {
                        Text(
                            text = if (isSimulatingLoading) "Live" else "Skeleton",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSimulatingLoading) BrandBluePrimary else Color(0xFF64748B),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Filter sheet button (AD11)
                    ActionButton(
                        onClick = { showFilterSheet = true },
                        containerColor = if (filterStatus != "All" || filterBlock != "All") Color(0xFFEFF6FF) else Color.White,
                        borderColor = if (filterStatus != "All" || filterBlock != "All") BrandBluePrimary else BorderLight,
                        size = 38.dp
                    ) {
                        FilterSlidersIcon(
                            size = 18.dp,
                            color = if (filterStatus != "All" || filterBlock != "All") BrandBluePrimary else Color(0xFF64748B)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Add Plot button (triggers AD18 Plot Added Toast)
                    ActionButton(
                        onClick = {
                            val nextNum = plots.size + 115
                            val newPlot = Plot(
                                id = "plot-$nextNum",
                                title = "Plot A-$nextNum",
                                block = "Block A",
                                location = "Bhopal Ring Rd",
                                sizeSqFt = "1,200 sq.ft",
                                status = PlotStatus.AVAILABLE,
                                pricePerSqFt = "₹4,166",
                                totalPriceFormatted = "₹50,00,000"
                            )
                            onAddPlot?.invoke(newPlot)
                            toastMessage = "Plot A-$nextNum added successfully"
                        },
                        containerColor = BrandBluePrimary,
                        borderColor = BrandBluePrimary,
                        size = 38.dp
                    ) {
                        Text(
                            text = "+",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            )

            // Search Bar & Filter quick pill row
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            ) {
                // Search Input Field
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF8FAFC),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SearchOutlineIcon(size = 18.dp, color = Color(0xFF64748B))

                        Spacer(modifier = Modifier.width(10.dp))

                        Box(modifier = Modifier.weight(1f)) {
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = if (isUserModeDemo) "Search user name or phone..." else "Search plot (e.g. A-114)...",
                                    fontSize = 13.5.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                            BasicTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                singleLine = true,
                                textStyle = TextStyle(
                                    fontSize = 13.5.sp,
                                    color = Color(0xFF0F172A),
                                    fontWeight = FontWeight.Medium
                                ),
                                cursorBrush = SolidColor(BrandBluePrimary),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        if (searchQuery.isNotEmpty()) {
                            Text(
                                text = "✕",
                                fontSize = 13.sp,
                                color = Color(0xFF94A3B8),
                                modifier = Modifier
                                    .clickable { searchQuery = "" }
                                    .padding(4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Block Quick Filter Pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf("All", "A", "B", "C").forEach { block ->
                        val isSelected = filterBlock == block
                        Surface(
                            onClick = { filterBlock = block },
                            shape = RoundedCornerShape(50),
                            color = if (isSelected) BrandBluePrimary else Color.White,
                            border = if (isSelected) null else BorderStroke(1.dp, Color(0xFFE2E8F0))
                        ) {
                            Box(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (block == "All") "All Blocks" else "Block $block",
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else Color(0xFF64748B)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Demo Toggle for "User Management" empty state (AD14)
                    Surface(
                        onClick = { isUserModeDemo = !isUserModeDemo },
                        shape = RoundedCornerShape(50),
                        color = if (isUserModeDemo) Color(0xFFFEF2F2) else Color(0xFFF1F5F9),
                        border = BorderStroke(1.dp, if (isUserModeDemo) Color(0xFFFECACA) else Color(0xFFE2E8F0))
                    ) {
                        Text(
                            text = if (isUserModeDemo) "View Plots" else "AD14 Demo",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isUserModeDemo) Color(0xFFDC2626) else Color(0xFF64748B),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Main Body: AD13 Skeleton, AD14 Empty State, or Plots List
            when {
                isSimulatingLoading -> {
                    // AD13 · Loading (table skeleton)
                    AdminTableSkeletonView(count = 4)
                }

                filteredPlots.isEmpty() -> {
                    // AD14 · Empty search results
                    AdminEmptySearchResultsView(
                        title = if (isUserModeDemo) "No matching users" else "No matching plots",
                        subtitle = if (isUserModeDemo) {
                            "Try a different name, mobile number or filter combination."
                        } else {
                            "Try a different plot number, block or filter combination."
                        },
                        actionText = "Reset Filters",
                        onActionClick = {
                            searchQuery = ""
                            filterBlock = "All"
                            filterStatus = "All"
                            isUserModeDemo = false
                        }
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(filteredPlots, key = { it.id }) { plot ->
                            AdminPlotCard(
                                plot = plot,
                                onOpenStatusDialog = { plotToToggleStatus = plot },
                                onOpenPriceDialog = { plotToUpdatePrice = plot },
                                onOpenDeleteDialog = { plotToDelete = plot }
                            )
                        }
                    }
                }
            }
        }

        // =====================================================================
        // AD18 · TOP TOAST BANNER
        // =====================================================================
        AnimatedVisibility(
            visible = toastMessage != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            toastMessage?.let { msg ->
                AdminPlotToast(
                    message = msg,
                    modifier = Modifier.padding(top = 10.dp),
                    onDismiss = { toastMessage = null }
                )
            }
        }
    }

    // =========================================================================
    // DIALOGS & BOTTOM SHEET
    // =========================================================================

    // AD11 · Advanced Filters Sheet
    if (showFilterSheet) {
        AdminAdvancedFilterSheet(
            selectedStatus = filterStatus,
            selectedBlock = filterBlock,
            priceRangeText = priceRangeText,
            onStatusChange = { filterStatus = it },
            onBlockChange = { filterBlock = it },
            onPriceRangeChange = { priceRangeText = it },
            onApply = {
                showFilterSheet = false
            },
            onDismiss = { showFilterSheet = false }
        )
    }

    // AD15 · Confirm Delete Plot Dialog
    plotToDelete?.let { plot ->
        ConfirmDeletePlotDialog(
            plotTitle = plot.title,
            onDismiss = { plotToDelete = null },
            onConfirmDelete = {
                onDeletePlot?.invoke(plot)
                toastMessage = "${plot.title} permanently deleted"
                plotToDelete = null
            }
        )
    }

    // AD16 · Confirm Price Change Dialog
    plotToUpdatePrice?.let { plot ->
        val oldPrice = plot.totalPriceFormatted
        val newPrice = if (oldPrice == "₹50,00,000") "₹52,00,000" else "₹50,00,000"
        ConfirmPriceChangeDialog(
            plotTitle = plot.title,
            oldPrice = oldPrice,
            newPrice = newPrice,
            onDismiss = { plotToUpdatePrice = null },
            onConfirmUpdate = {
                onUpdatePrice?.invoke(plot, newPrice)
                toastMessage = "${plot.title} price updated to $newPrice"
                plotToUpdatePrice = null
            }
        )
    }

    // AD17 · Confirm Status Update Dialog
    plotToToggleStatus?.let { plot ->
        val isAvail = plot.status == PlotStatus.AVAILABLE
        val targetStatus = if (isAvail) "Sold" else "Available"
        ConfirmStatusUpdateDialog(
            plotTitle = plot.title,
            targetStatus = targetStatus,
            onDismiss = { plotToToggleStatus = null },
            onConfirmUpdate = {
                onTogglePlotStatus(plot)
                toastMessage = "${plot.title} marked as $targetStatus"
                plotToToggleStatus = null
            }
        )
    }
}

@Composable
private fun AdminPlotCard(
    plot: Plot,
    onOpenStatusDialog: () -> Unit,
    onOpenPriceDialog: () -> Unit,
    onOpenDeleteDialog: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Row 1: Pin icon, Title, Block & Size, StatusBadge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color.White, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    MapPinIcon(size = 20.dp, color = BrandBluePrimary)
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = plot.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${plot.block} · ${plot.sizeSqFt}",
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                }

                StatusBadge(label = plot.status.label)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Row 2: Price info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = plot.totalPriceFormatted,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "${plot.pricePerSqFt}/sq.ft · ${plot.roadWidth} Road",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }

                // Row of Action buttons: Update Price, Status Change, Delete Plot
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Price update button (AD16)
                    Surface(
                        onClick = onOpenPriceDialog,
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFEFF6FF),
                        border = BorderStroke(1.dp, Color(0xFFBFDBFE))
                    ) {
                        Text(
                            text = "₹ Price",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BrandBluePrimary,
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp)
                        )
                    }

                    // Status change button (AD17)
                    val isAvailable = plot.status == PlotStatus.AVAILABLE
                    Surface(
                        onClick = onOpenStatusDialog,
                        shape = RoundedCornerShape(8.dp),
                        color = if (isAvailable) Color(0xFFFEF2F2) else Color(0xFFF0FDF4),
                        border = BorderStroke(1.dp, if (isAvailable) Color(0xFFFECACA) else Color(0xFFBBF7D0))
                    ) {
                        Text(
                            text = if (isAvailable) "Mark Sold" else "Mark Available",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isAvailable) Color(0xFFDC2626) else Color(0xFF16A34A),
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp)
                        )
                    }

                    // Delete button (AD15)
                    Surface(
                        onClick = onOpenDeleteDialog,
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Box(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            TrashOutlineIcon(size = 15.dp, color = Color(0xFFEF4444))
                        }
                    }
                }
            }
        }
    }
}
